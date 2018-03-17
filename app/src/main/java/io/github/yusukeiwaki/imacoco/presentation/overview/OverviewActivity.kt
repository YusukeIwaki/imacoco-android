package io.github.yusukeiwaki.imacoco.presentation.overview

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import io.github.yusukeiwaki.imacoco.R
import io.github.yusukeiwaki.imacoco.databinding.ActivityOverviewBinding
import io.github.yusukeiwaki.imacoco.presentation.base.BaseActivity
import io.github.yusukeiwaki.imacoco.presentation.positioning.PositioningRequirementCheckActivity
import io.github.yusukeiwaki.imacoco.repository.current_user.CurrentUserManager
import io.github.yusukeiwaki.imacoco.repository.device_registration.DeviceRegistrationManager
import io.github.yusukeiwaki.imacoco.repository.location_log.LocationLogManager

class OverviewActivity : BaseActivity() {
    companion object {
        private val TAG = "OverviewActivity"
        fun newIntent(context: Context): Intent {
            return Intent(context, OverviewActivity::class.java)
        }
    }

    private lateinit var binding: ActivityOverviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_overview)
        binding.btnLogout.setOnClickListener { _ ->
            CurrentUserManager().firebaseCurrentUser?.let { currentUser ->
                DeviceRegistrationManager(this@OverviewActivity).unregister(currentUser.uid)
                LocationLogManager().clear(currentUser.uid)
                CurrentUserManager().logout()
            }
        }
        CurrentUserManager().firebaseCurrentUserAsLiveData().observe(this, Observer { currentUser ->
            binding.setCurrentUser(currentUser)
        })
        startActivity(PositioningRequirementCheckActivity.newIntent(this))
    }
}
