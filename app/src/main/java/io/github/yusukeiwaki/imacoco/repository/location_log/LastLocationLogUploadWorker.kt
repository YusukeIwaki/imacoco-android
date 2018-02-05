package io.github.yusukeiwaki.imacoco.repository.location_log

import com.google.firebase.database.FirebaseDatabase

internal class LastLocationLogUploadWorker(private val uid: String, private val lastLocationLog: LastLocationLog) {
    fun perform() {
        FirebaseDatabase.getInstance().reference
                .child("public").child("users").child(uid).child("last_location").setValue(lastLocationLog)
    }
}
