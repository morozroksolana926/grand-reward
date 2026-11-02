package com.Qerxavi7.Nomlut.domain.usecase

import com.Qerxavi7.Nomlut.domain.model.GameResult
import com.Qerxavi7.Nomlut.domain.model.GoalKind

class UpdateDailyGoalsUseCase {

    operator fun invoke(current: Map<GoalKind, Int>, result: GameResult): Map<GoalKind, Int> {
        val tokens = (current[GoalKind.TOKENS_CLEARED] ?: 0) + result.tokensCleared
        val bestStageScore = maxOf(current[GoalKind.SINGLE_STAGE_SCORE] ?: 0, result.score)
        val stages = (current[GoalKind.STAGES_COMPLETED] ?: 0) + if (result.stageCleared) 1 else 0
        return mapOf(
            GoalKind.TOKENS_CLEARED to tokens,
            GoalKind.SINGLE_STAGE_SCORE to bestStageScore,
            GoalKind.STAGES_COMPLETED to stages
        )
    }
}
