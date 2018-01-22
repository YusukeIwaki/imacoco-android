package io.github.yusukeiwaki.imacoco.presentation.base

import android.app.Service
import android.content.Intent

abstract class BaseOneShotTaskService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = START_NOT_STICKY

    override fun onBind(intent: Intent) = null
}
