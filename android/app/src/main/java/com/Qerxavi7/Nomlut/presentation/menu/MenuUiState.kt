package com.Qerxavi7.Nomlut.presentation.menu

import com.Qerxavi7.Nomlut.core.config.GameConfig

data class MenuUiState(
    val rewardPoints: Int = 0,
    val bestScore: Int = 0,
    val stage: Int = 1,
    val relicsUnlocked: Int = 0,
    val relicsTotal: Int = GameConfig.RELIC_COUNT,
    val soundCues: Boolean = true,
    val haptics: Boolean = true,
    val reducedMotion: Boolean = false,
    val settingsVisible: Boolean = false,
    val confirmResetVisible: Boolean = false
) {
    val hasRecord: Boolean get() = bestScore > 0
}
