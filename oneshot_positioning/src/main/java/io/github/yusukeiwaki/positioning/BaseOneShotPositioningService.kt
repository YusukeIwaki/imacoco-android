package io.github.yusukeiwaki.positioning

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.os.Looper
import android.support.annotation.RequiresPermission
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import kotlinx.coroutines.experimental.withTimeoutOrNull

abstract class BaseOneShotPositioningService : Service() {
    private lateinit var client: FusedLocationProviderClient

    override fun onCreate() {
        super.onCreate()

        if (!hasLocationPermission()) {
            stopSelf()
            return
        }

        client = LocationServices.getFusedLocationProviderClient(this)
        val locationUpdater = LocationUpdater(client, Looper.myLooper())
        launch {
            withTimeoutOrNull(PositioningUtil.positioningTimeout) {
                locationUpdater.requestUpdate()
            }?.let { onNewLocation(it) }
            locationUpdater.removeUpdateRequest()
            stopSelf()
        }
    }

    private fun hasLocationPermission() =
            PositioningUtil.requiredPermissions.any { permission ->
                ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
            }

    class NoLocationAvailableError: Exception()

    private inner class LocationUpdater(private val client: FusedLocationProviderClient, private val looper: Looper?) {
        private var locationCallback: LocationCallback? = null

        @RequiresPermission(anyOf = arrayOf("android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"))
        suspend fun requestUpdate() = suspendCancellableCoroutine<Location> { cont ->
            Log.d(TAG, "LocationUpdater:requestUpdate")
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult?.let {
                        Log.d(TAG, "LocationUpdater:resolve${locationResult.lastLocation}")
                        cont.resume(it.lastLocation)
                    } ?: run {
                        Log.d(TAG, "LocationUpdater:reject")
                        cont.resumeWithException(NoLocationAvailableError())
                    }
                }
            }
            client.requestLocationUpdates(PositioningUtil.locationRequest, locationCallback, looper)
        }

        fun removeUpdateRequest() {
            Log.d(TAG, "LocationUpdater:removeUpdateRequest")
            client.removeLocationUpdates(locationCallback!!)
        }
    }

    protected abstract fun onNewLocation(location: Location)

    override fun onBind(intent: Intent): IBinder? = null

    companion object {
        private val TAG = "BaseOneShotPositioningService"
    }
}
