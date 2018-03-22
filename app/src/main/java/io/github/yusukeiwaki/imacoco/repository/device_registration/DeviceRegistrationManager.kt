package io.github.yusukeiwaki.imacoco.repository.device_registration

import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import io.github.yusukeiwaki.imacoco.repository.current_user.CurrentUserManager

class DeviceRegistrationManager {

    /**
     * FCMトークン更新時 or ログインUserのuid更新時に呼ぶ
     */
    fun scheduleRegister() {
        val uid = CurrentUserManager().firebaseCurrentUser?.uid
        val token = FirebaseInstanceId.getInstance().token
        if (!uid.isNullOrBlank() && !token.isNullOrBlank()) {
            register(uid!!, token!!)
        }
    }

    private fun register(uid: String, token: String) =
            FirebaseDatabase.getInstance().reference.child("users").child(uid).child("fcm_token").setValue(token)

    fun unregister(uid: String): Task<Void> {
        return FirebaseDatabase.getInstance().reference.child("users").child(uid).removeValue()
    }
}
