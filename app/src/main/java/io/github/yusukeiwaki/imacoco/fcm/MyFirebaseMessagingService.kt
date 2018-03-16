package io.github.yusukeiwaki.imacoco.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.github.yusukeiwaki.imacoco.presentation.positioning.OneShotPositioningService

class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        val TAG = "MyFirebaseMessagingService"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.d(TAG, "onMessageReceived: ${remoteMessage}")
        if (remoteMessage != null) {
            remoteMessage.data.remove("push_type")?.let { pushType ->
                handleReceivedMessage(pushType, remoteMessage.data)
            }
        }
    }

    private fun handleReceivedMessage(pushType: String, data: Map<String, String>) {
        when(pushType) {
            "positioning_request" -> {
                startService(OneShotPositioningService.newIntent(this))
            }
        }
    }
}
