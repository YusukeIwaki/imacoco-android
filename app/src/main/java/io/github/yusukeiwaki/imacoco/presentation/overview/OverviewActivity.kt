package io.github.yusukeiwaki.imacoco.presentation.overview

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import io.github.yusukeiwaki.imacoco.R
import io.github.yusukeiwaki.imacoco.databinding.ActivityOverviewBinding
import io.github.yusukeiwaki.imacoco.presentation.base.BaseActivity
import io.github.yusukeiwaki.imacoco.presentation.base.FirebaseCurrentUserLiveData


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
            FirebaseAuth.getInstance().signOut()
        }
        FirebaseCurrentUserLiveData(FirebaseAuth.getInstance()).observe(this, Observer { currentUser ->
            binding.setCurrentUser(currentUser)
        })
    }
}
