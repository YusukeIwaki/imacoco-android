package io.github.yusukeiwaki.imacoco.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        val TAG = "MyFirebaseMessagingService"
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.d(TAG, "onMessageReceived: ${remoteMessage}")
    }
}
