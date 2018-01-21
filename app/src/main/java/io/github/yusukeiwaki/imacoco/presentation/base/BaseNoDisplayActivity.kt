package io.github.yusukeiwaki.imacoco.presentation.base

import android.app.Activity
import android.os.Bundle

/**
 * Theme.NoDisplay指定のベースクラス。
 * onCreateでfinishすることを保証する。
 */
abstract class BaseNoDisplayActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doAction()
        finish()
    }

    protected abstract fun doAction()
}
