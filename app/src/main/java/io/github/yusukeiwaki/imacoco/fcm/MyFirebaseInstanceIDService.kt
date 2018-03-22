package io.github.yusukeiwaki.imacoco.fcm

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import io.github.yusukeiwaki.imacoco.repository.device_registration.DeviceRegistrationManager

class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {
    companion object {
        val TAG = "MyFirebaseInstanceIDService"
    }

    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token!!
        Log.d(TAG, "Refreshed token: " + refreshedToken)

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken)
    }

    private fun sendRegistrationToServer(token: String) {
        DeviceRegistrationManager().scheduleRegister()
    }
}
