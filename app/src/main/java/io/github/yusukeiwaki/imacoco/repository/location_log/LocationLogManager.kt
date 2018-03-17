package io.github.yusukeiwaki.imacoco.repository.location_log

import android.location.Location
import com.google.firebase.database.FirebaseDatabase
import io.github.yusukeiwaki.imacoco.repository.current_user.CurrentUserManager

class LocationLogManager {
    fun updateLastLocation(location: Location) {
        val lastLocationLog = LastLocationLog(
                latitude = location.latitude,
                longitude = location.longitude,
                accuracy = location.accuracy.toDouble(),
                timestampMs = location.time
        )
        CurrentUserManager().firebaseCurrentUser?.let { currentUser ->
            upload(currentUser.uid, lastLocationLog)
        }
    }

    private fun upload(uid: String, lastLocationLog: LastLocationLog) =
            FirebaseDatabase.getInstance().reference
                    .child("public").child("users").child(uid).child("last_location").setValue(lastLocationLog)

    fun clear(uid: String) =
            FirebaseDatabase.getInstance().reference
                    .child("public").child("users").child(uid).removeValue()
}
