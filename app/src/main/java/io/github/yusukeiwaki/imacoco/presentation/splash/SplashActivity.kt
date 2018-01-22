package io.github.yusukeiwaki.imacoco.presentation.splash

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import io.github.yusukeiwaki.imacoco.R
import io.github.yusukeiwaki.imacoco.presentation.base.BaseActivity
import io.github.yusukeiwaki.imacoco.presentation.base.FirebaseCurrentUserLiveData
import io.github.yusukeiwaki.imacoco.presentation.overview.OverviewActivity

class SplashActivity : BaseActivity() {
    companion object {
        val TAG = "SplashActivity"
        val KEY_NEXT_INTENT = "next_intent"

        fun newIntent(context: Context, nextIntent: Intent? = null): Intent {
            return Intent(context, SplashActivity::class.java).also { intent ->
                nextIntent?.let { intent.putExtra(KEY_NEXT_INTENT, it) }
            }
        }
    }

    private val nextIntent: Intent? by lazy {
        intent?.getParcelableExtra<Intent>(KEY_NEXT_INTENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val firebaseAuth = FirebaseAuth.getInstance()
        FirebaseCurrentUserLiveData(firebaseAuth).observe(this, Observer { currentUser ->
            if (currentUser != null) {
                proceedToNextActivity()
            }
        })
        if (firebaseAuth.currentUser == null) {
            firebaseAuth.signInAnonymously()
                    .addOnCompleteListener(this, { task ->
                        if (!task.isSuccessful) {
                            Log.w(TAG, "auth failure", task.exception)
                            Toast.makeText(this@SplashActivity, "認証に失敗しました", Toast.LENGTH_SHORT).show()
                        }
                    })
        }
    }

    private fun proceedToNextActivity() {
        if (nextIntent == null) {
            startActivity(OverviewActivity.newIntent(this))
        } else {
            startActivity(nextIntent)
        }
        finish()
    }
}
