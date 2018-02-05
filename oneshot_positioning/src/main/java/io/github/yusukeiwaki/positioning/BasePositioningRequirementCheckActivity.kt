package io.github.yusukeiwaki.positioning

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes

abstract class BasePositioningRequirementCheckActivity : Activity() {
    private var hasValidLocationSettingsCache: Boolean = false

    /**
     * Called when showing errors are required.
     * code is one of
     *
     * * [RequirementCheckFailure.NO_LOCATION_SETTING] = Location settings are disabled.
     * * [RequirementCheckFailure.NO_LOCATION_PERMISSION] = Not allowed to execute positioning.
     *
     */
    protected abstract fun showError(code: RequirementCheckFailure)

    /**
     * Called after checing permission and settings.
     * Typically, request location updates actually here.
     */
    protected abstract fun afterCheck()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hasValidLocationSettingsCache = false
        doCheck()
    }

    private fun doCheck() {
        if (!hasValidLocationSettings()) {
            return
        }

        if (!hasLocationPermission()) {
            ActivityCompat.requestPermissions(this, PositioningUtil.requiredPermissions, RC_PERMISSION)
            return
        }

        finish()
        afterCheck()
    }

    private fun hasValidLocationSettings(): Boolean {
        if (hasValidLocationSettingsCache) return true

        val locationSettingsRequest = LocationSettingsRequest.Builder()
                .addLocationRequest(PositioningUtil.locationRequest)
                .build()

        LocationServices.getSettingsClient(this).checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener { _ ->
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    hasValidLocationSettingsCache = true
                    doCheck()
                }
                .addOnFailureListener { exception ->
                    (exception as? ApiException)?.let { apiException ->
                        when (apiException.getStatusCode()) {
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                                hasValidLocationSettingsCache = false
                                // Location settings are not satisfied. But could be fixed by showing the
                                // user a dialog.
                                try {
                                    // Cast to a resolvable exception.
                                    val resolvable = apiException as ResolvableApiException
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    resolvable.startResolutionForResult(this@BasePositioningRequirementCheckActivity, RC_LOCATION_SETTING)
                                } catch (e: IntentSender.SendIntentException) {
                                    showError(RequirementCheckFailure.NO_LOCATION_SETTING)
                                    finish()
                                } catch (e: ClassCastException) {
                                    showError(RequirementCheckFailure.NO_LOCATION_SETTING)
                                    finish()
                                }
                            }

                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                                hasValidLocationSettingsCache = false
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won't show the dialog.
                                showError(RequirementCheckFailure.NO_LOCATION_SETTING)
                                finish()
                            }
                        }
                    }
                }
        return false
    }

    private fun hasLocationPermission() =
            PositioningUtil.requiredPermissions.any { permission ->
                ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
            }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != RC_PERMISSION) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }

        if (grantResults.any { it == PackageManager.PERMISSION_GRANTED }) {
            doCheck()
            return
        }

        showError(RequirementCheckFailure.NO_LOCATION_PERMISSION)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode != RC_LOCATION_SETTING) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }

        if (resultCode == Activity.RESULT_OK) {
            doCheck()
            return
        }

        showError(RequirementCheckFailure.NO_LOCATION_SETTING)
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    companion object {
        private val RC_PERMISSION = 10
        private val RC_LOCATION_SETTING = 11
    }
}