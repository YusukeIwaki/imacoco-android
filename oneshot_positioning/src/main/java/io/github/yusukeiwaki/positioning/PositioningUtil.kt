package io.github.yusukeiwaki.positioning

import android.Manifest
import com.google.android.gms.location.LocationRequest

internal object PositioningUtil {
    val requiredPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)

    val locationRequest = LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setFastestInterval(4000)
            .setInterval(12000)

    val positioningTimeout = 13000
}
