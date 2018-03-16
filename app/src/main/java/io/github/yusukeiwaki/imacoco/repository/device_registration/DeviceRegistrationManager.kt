package io.github.yusukeiwaki.imacoco.repository.device_registration

import android.content.Context

class DeviceRegistrationManager(private val context: Context) {

    private val cache: DeviceRegistrationCache
        get() = DeviceRegistrationCache(context)

    /**
     * Anonymous userが取得できたときに呼ばれる
     */
    fun updateCurrentUserId(uid: String) {
        cache.uid = uid
        onUpdate()
    }

    /**
     * FCMトークンがアップデートされたときに呼ばれる
     */
    fun updateDeviceToken(token: String) {
        cache.token = token
        onUpdate()
    }

    private fun onUpdate() {
        val uid = cache.uid
        val token = cache.token
        if (!uid.isNullOrBlank() && !token.isNullOrBlank()) {
            DeviceRegistrationWorker(uid!!, token!!).perform()
        }
    }
}
