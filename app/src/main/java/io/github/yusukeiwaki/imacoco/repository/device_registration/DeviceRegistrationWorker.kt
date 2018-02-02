package io.github.yusukeiwaki.imacoco.repository.device_registration

import com.google.firebase.database.FirebaseDatabase

class DeviceRegistrationWorker(private val uid: String, private val token: String) {
    fun perform() {
        FirebaseDatabase.getInstance().reference
                .child("users").child(uid).child("token").setValue(token)
    }
}
