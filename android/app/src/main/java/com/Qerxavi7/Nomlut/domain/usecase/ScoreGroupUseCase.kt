package com.Qerxavi7.Nomlut.domain.usecase

import com.Qerxavi7.Nomlut.core.config.GameConfig

class ScoreGroupUseCase {

    operator fun invoke(groupSize: Int, chainIndex: Int): Int {
        val base = groupSize * groupSize * 10
        return (base * multiplier(chainIndex)).toInt()
    }

    fun multiplier(chainIndex: Int): Float {
        val raw = 1f + GameConfig.CHAIN_STEP * chainIndex
        return if (raw > GameConfig.CHAIN_CAP) GameConfig.CHAIN_CAP else raw
    }
}
