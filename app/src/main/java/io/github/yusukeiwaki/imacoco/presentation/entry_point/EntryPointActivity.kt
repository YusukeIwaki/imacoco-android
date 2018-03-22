package io.github.yusukeiwaki.imacoco.presentation.entry_point

import io.github.yusukeiwaki.imacoco.presentation.base.BaseNoDisplayActivity
import io.github.yusukeiwaki.imacoco.presentation.overview.OverviewActivity
import io.github.yusukeiwaki.imacoco.presentation.splash.SplashActivity
import io.github.yusukeiwaki.imacoco.repository.current_user.CurrentUserManager
import io.github.yusukeiwaki.imacoco.repository.device_registration.DeviceRegistrationManager

class EntryPointActivity : BaseNoDisplayActivity() {
    override fun doAction() {
        val firebaseCurrentUser = CurrentUserManager().firebaseCurrentUser
        if (firebaseCurrentUser != null) {
            startActivity(OverviewActivity.newIntent(this))
            DeviceRegistrationManager().scheduleRegister()
        } else {
            startActivity(SplashActivity.newIntent(this))
        }
    }
}
