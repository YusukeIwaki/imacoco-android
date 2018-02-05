package io.github.yusukeiwaki.imacoco.presentation.positioning

import android.content.Context
import android.content.Intent
import android.location.Location
import io.github.yusukeiwaki.imacoco.repository.location_log.LocationLogManager
import io.github.yusukeiwaki.positioning.BaseOneShotPositioningService

class OneShotPositioningService : BaseOneShotPositioningService() {
    override fun onNewLocation(location: Location) {
        LocationLogManager().updateLastLocation(location)
    }

    companion object {
        private val TAG = "OneShotPositioningService"

        fun newIntent(context: Context) = Intent(context, OneShotPositioningService::class.java)
    }
}
