package io.github.yusukeiwaki.imacoco.repository.location_log

internal data class LastLocationLog(
        val latitude: Double,
        val longitude: Double,
        val accuracy: Double,
        val timestampMs: Long)
