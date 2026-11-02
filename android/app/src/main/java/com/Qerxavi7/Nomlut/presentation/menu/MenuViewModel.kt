package com.Qerxavi7.Nomlut.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Qerxavi7.Nomlut.domain.repository.ProgressRepository
import com.Qerxavi7.Nomlut.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.profile.collect { profile ->
                _state.update {
                    it.copy(
                        rewardPoints = profile.rewardPoints,
                        bestScore = profile.bestScore,
                        stage = profile.stage,
                        relicsUnlocked = profile.unlockedRelics.size
                    )
                }
            }
        }
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                _state.update {
                    it.copy(
                        soundCues = settings.soundCues,
                        haptics = settings.haptics,
                        reducedMotion = settings.reducedMotion
                    )
                }
            }
        }
    }

    fun openSettings() {
        _state.update { it.copy(settingsVisible = true) }
    }

    fun closeSettings() {
        _state.update { it.copy(settingsVisible = false) }
    }

    fun setSoundCues(enabled: Boolean) {
        settingsRepository.setSoundCues(enabled)
    }

    fun setHaptics(enabled: Boolean) {
        settingsRepository.setHaptics(enabled)
    }

    fun setReducedMotion(enabled: Boolean) {
        settingsRepository.setReducedMotion(enabled)
    }

    fun requestReset() {
        _state.update { it.copy(confirmResetVisible = true) }
    }

    fun cancelReset() {
        _state.update { it.copy(confirmResetVisible = false) }
    }

    fun confirmReset() {
        progressRepository.resetProgress()
        _state.update { it.copy(confirmResetVisible = false, settingsVisible = false) }
    }
}
