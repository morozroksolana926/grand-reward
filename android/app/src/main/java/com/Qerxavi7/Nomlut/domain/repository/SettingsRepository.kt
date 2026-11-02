package com.Qerxavi7.Nomlut.domain.repository

import com.Qerxavi7.Nomlut.domain.model.AppSettings
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {

    val settings: StateFlow<AppSettings>

    fun setSoundCues(enabled: Boolean)

    fun setHaptics(enabled: Boolean)

    fun setReducedMotion(enabled: Boolean)
}
