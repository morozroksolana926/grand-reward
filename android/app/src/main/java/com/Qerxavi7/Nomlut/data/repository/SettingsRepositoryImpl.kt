package com.Qerxavi7.Nomlut.data.repository

import com.Qerxavi7.Nomlut.data.local.PreferencesStorage
import com.Qerxavi7.Nomlut.domain.model.AppSettings
import com.Qerxavi7.Nomlut.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(
    private val storage: PreferencesStorage
) : SettingsRepository {

    private val _settings = MutableStateFlow(
        AppSettings(
            soundCues = storage.readBoolean(KEY_SOUND, true),
            haptics = storage.readBoolean(KEY_HAPTICS, true),
            reducedMotion = storage.readBoolean(KEY_REDUCED_MOTION, false)
        )
    )
    override val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    override fun setSoundCues(enabled: Boolean) {
        storage.writeBoolean(KEY_SOUND, enabled)
        _settings.value = _settings.value.copy(soundCues = enabled)
    }

    override fun setHaptics(enabled: Boolean) {
        storage.writeBoolean(KEY_HAPTICS, enabled)
        _settings.value = _settings.value.copy(haptics = enabled)
    }

    override fun setReducedMotion(enabled: Boolean) {
        storage.writeBoolean(KEY_REDUCED_MOTION, enabled)
        _settings.value = _settings.value.copy(reducedMotion = enabled)
    }

    private companion object {
        const val KEY_SOUND = "setting_sound"
        const val KEY_HAPTICS = "setting_haptics"
        const val KEY_REDUCED_MOTION = "setting_reduced_motion"
    }
}
