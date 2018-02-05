package io.github.yusukeiwaki.imacoco.repository.location_log

import android.location.Location
import com.google.firebase.auth.FirebaseAuth

class LocationLogManager {
    fun updateLastLocation(location: Location) {
        val lastLocationLog = LastLocationLog(
                latitude = location.latitude,
                longitude = location.longitude,
                accuracy = location.accuracy.toDouble(),
                timestampMs = location.time
        )
        FirebaseAuth.getInstance().currentUser?.let { currentUser ->
            LastLocationLogUploadWorker(currentUser.uid, lastLocationLog).perform()
        }
    }
}
