package io.github.yusukeiwaki.imacoco.presentation.entry_point

import com.google.firebase.auth.FirebaseAuth
import io.github.yusukeiwaki.imacoco.presentation.base.BaseNoDisplayActivity
import io.github.yusukeiwaki.imacoco.presentation.overview.OverviewActivity
import io.github.yusukeiwaki.imacoco.presentation.splash.SplashActivity

class EntryPointActivity : BaseNoDisplayActivity() {
    override fun doAction() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(OverviewActivity.newIntent(this))
        } else {
            startActivity(SplashActivity.newIntent(this))
        }
    }
}
