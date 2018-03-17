package io.github.yusukeiwaki.imacoco.presentation.entry_point

import com.google.firebase.iid.FirebaseInstanceId
import io.github.yusukeiwaki.imacoco.presentation.base.BaseNoDisplayActivity
import io.github.yusukeiwaki.imacoco.presentation.overview.OverviewActivity
import io.github.yusukeiwaki.imacoco.presentation.splash.SplashActivity
import io.github.yusukeiwaki.imacoco.repository.current_user.CurrentUserManager
import io.github.yusukeiwaki.imacoco.repository.device_registration.DeviceRegistrationManager

class EntryPointActivity : BaseNoDisplayActivity() {
    override fun doAction() {
        CurrentUserManager().firebaseCurrentUser?.let { currentUser ->
            startActivity(OverviewActivity.newIntent(this))
            DeviceRegistrationManager(this).updateCurrentUserId(currentUser.uid)
        } ?: run {
            startActivity(SplashActivity.newIntent(this))
        }
        FirebaseInstanceId.getInstance().token?.let { token ->
            DeviceRegistrationManager(this).updateDeviceToken(token)
        }
    }
}
