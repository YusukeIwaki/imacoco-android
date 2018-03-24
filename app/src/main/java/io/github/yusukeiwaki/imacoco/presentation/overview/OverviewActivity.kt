package io.github.yusukeiwaki.imacoco.presentation.overview

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.Toast
import io.github.yusukeiwaki.imacoco.R
import io.github.yusukeiwaki.imacoco.databinding.ActivityOverviewBinding
import io.github.yusukeiwaki.imacoco.presentation.base.BaseActivity
import io.github.yusukeiwaki.imacoco.presentation.positioning.PositioningRequirementCheckActivity
import io.github.yusukeiwaki.imacoco.repository.current_user.CurrentUserManager

class OverviewActivity : BaseActivity() {
    companion object {
        private val TAG = "OverviewActivity"
        fun newIntent(context: Context): Intent {
            return Intent(context, OverviewActivity::class.java)
        }
    }

    private lateinit var viewModel: OverviewActivityViewModel
    private lateinit var binding: ActivityOverviewBinding
    private lateinit var logoutProgressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OverviewActivityViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_overview)

        // ログアウトボタン
        binding.toolbar.inflateMenu(R.menu.activity_overview)
        binding.toolbar.menu.findItem(R.id.logout).setOnMenuItemClickListener {
            viewModel.logout()
            true
        }
        logoutProgressDialog = ProgressDialog(this).also { dialog ->
            dialog.setMessage("終了しています...")
            dialog.setCancelable(false)
        }
        viewModel.logoutState().observe(this, Observer { progressState ->
            showOrHideLogoutProgress(progressState?.isInProgress ?: false)

            if (progressState != null) {
                if (progressState.isSuccess) {
                    onLogoutSuccess()
                    viewModel.resetLogoutState()
                } else if (progressState.isError) {
                    showLogoutError(progressState.error!!)
                    viewModel.resetLogoutState()
                }
            }
        })

        // 共有URL
        CurrentUserManager().shareUrlAsLiveData().observe(this, Observer { url ->
            binding.shareUrl = url
        })
        binding.shareButton.setOnClickListener {
            binding.shareUrl?.let { url ->
                share(url)
            }
        }

        // 測位のパーミッション確認
        startActivity(PositioningRequirementCheckActivity.newIntent(this))
    }

    override fun onBackPressed() {
        moveTaskToBack(false)
    }

    private fun onLogoutSuccess() {
        finish()
    }

    private fun showLogoutError(error: Exception) {
        Toast.makeText(this, "ログアウト時にエラーが発生しました", Toast.LENGTH_SHORT).show()
    }

    private fun showOrHideLogoutProgress(logoutIsInProgress: Boolean) {
        if (logoutIsInProgress) {
            if (!logoutProgressDialog.isShowing) {
                logoutProgressDialog.show()
            }
        } else {
            if (logoutProgressDialog.isShowing) {
                logoutProgressDialog.dismiss()
            }
        }
    }

    override fun onStop() {
        showOrHideLogoutProgress(false)
        super.onStop()
    }

    private fun share(url: String) {
        val baseIntent = Intent(Intent.ACTION_SEND).also { intent ->
            intent.setType("text/plain")
            intent.putExtra(Intent.EXTRA_TEXT, url)
        }
        val chooserIntent = Intent.createChooser(baseIntent, "URLを共有")
        startActivity(chooserIntent)
    }
}
