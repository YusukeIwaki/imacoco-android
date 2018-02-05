package io.github.yusukeiwaki.imacoco.presentation.positioning

import android.content.Context
import android.content.Intent
import android.widget.Toast
import io.github.yusukeiwaki.positioning.BasePositioningRequirementCheckActivity
import io.github.yusukeiwaki.positioning.RequirementCheckFailure

class PositioningRequirementCheckActivity : BasePositioningRequirementCheckActivity() {
    private val nextIntent by lazy<Intent?> { intent?.getParcelableExtra(KEY_NEXT_INTENT) }

    override fun showError(code: RequirementCheckFailure) {
        when(code) {
            RequirementCheckFailure.NO_LOCATION_SETTING -> {
                Toast.makeText(this, "no location settings", Toast.LENGTH_SHORT).show()
            }
            RequirementCheckFailure.NO_LOCATION_PERMISSION -> {
                Toast.makeText(this, "no location permissions", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun afterCheck() {
        nextIntent?.let { startService(it) }
    }

    companion object {
        private val KEY_NEXT_INTENT = "next_intent"

        fun newIntent(context: Context, nextIntent: Intent = OneShotPositioningService.newIntent(context)) =
                Intent(context, PositioningRequirementCheckActivity::class.java).also { intent ->
                    intent.putExtra(KEY_NEXT_INTENT, nextIntent)
                }
    }
}
