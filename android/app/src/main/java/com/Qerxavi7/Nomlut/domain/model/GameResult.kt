package com.Qerxavi7.Nomlut.domain.model

import com.Qerxavi7.Nomlut.core.config.GameConfig

data class GameResult(
    val stage: Int,
    val score: Int,
    val tokensCleared: Int,
    val bestGroup: Int,
    val movesLeft: Int,
    val stageCleared: Boolean
) {
    val rewardPoints: Int
        get() = score / GameConfig.RP_PER_POINT + if (stageCleared) GameConfig.STAGE_CLEAR_RP else 0
}
