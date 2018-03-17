package io.github.yusukeiwaki.imacoco.repository.device_registration

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase

class DeviceRegistrationManager(private val context: Context) {

    private fun cache() = DeviceRegistrationCache(context)

    /**
     * Anonymous userが取得できたときに呼ばれる
     */
    fun updateCurrentUserId(uid: String) {
        cache().uid = uid
        onUpdate()
    }

    /**
     * FCMトークンがアップデートされたときに呼ばれる
     */
    fun updateDeviceToken(token: String) {
        cache().token = token
        onUpdate()
    }

    private fun onUpdate() {
        val cache = cache()
        val uid = cache.uid
        val token = cache.token
        if (!uid.isNullOrBlank() && !token.isNullOrBlank()) {
            register(uid!!, token!!)
        }
    }

    private fun register(uid: String, token: String) =
            FirebaseDatabase.getInstance().reference.child("users").child(uid).child("token").setValue(token)

    fun unregister(uid: String): Task<Void> {
        cache().uid = null
        return FirebaseDatabase.getInstance().reference.child("users").child(uid).removeValue()
    }
}
