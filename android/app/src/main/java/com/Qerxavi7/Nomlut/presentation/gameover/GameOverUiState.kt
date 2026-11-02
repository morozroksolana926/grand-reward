package com.Qerxavi7.Nomlut.presentation.gameover

import com.Qerxavi7.Nomlut.domain.model.Relic

data class GameOverUiState(
    val loaded: Boolean = false,
    val stageCleared: Boolean = false,
    val score: Int = 0,
    val tokensCleared: Int = 0,
    val bestGroup: Int = 0,
    val rewardPointsEarned: Int = 0,
    val totalRewardPoints: Int = 0,
    val bestScore: Int = 0,
    val relic: Relic? = null,
    val relicUnlocked: Boolean = false,
    val nextStage: Int = 1,
    val hasNextStage: Boolean = false
)
