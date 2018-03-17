package io.github.yusukeiwaki.imacoco.repository.device_registration

import android.content.Context
import io.github.yusukeiwaki.sharedpref_property.StringPref

internal class DeviceRegistrationCache(private val context: Context) {
    private val sharedPreferences by lazy { context.getSharedPreferences("device_registration", Context.MODE_PRIVATE) }

    var uid by StringPref(sharedPreferences)
    var token by StringPref(sharedPreferences)
}
