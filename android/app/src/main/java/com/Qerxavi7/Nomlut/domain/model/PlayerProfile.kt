package com.Qerxavi7.Nomlut.domain.model

data class PlayerProfile(
    val rewardPoints: Int = 0,
    val bestScore: Int = 0,
    val stage: Int = 1,
    val stagesCleared: Int = 0,
    val tokensCleared: Int = 0,
    val unlockedRelics: Set<Int> = emptySet()
)
