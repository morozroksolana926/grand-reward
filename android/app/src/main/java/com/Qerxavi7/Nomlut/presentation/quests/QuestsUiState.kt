package com.Qerxavi7.Nomlut.presentation.quests

import com.Qerxavi7.Nomlut.domain.model.DailyGoalProgress
import com.Qerxavi7.Nomlut.domain.model.StageDef

enum class StageStatus {
    CLEARED,
    CURRENT,
    LOCKED
}

data class StageNode(
    val stage: StageDef,
    val status: StageStatus
)

data class QuestsUiState(
    val goals: List<DailyGoalProgress> = emptyList(),
    val nodes: List<StageNode> = emptyList(),
    val currentStage: Int = 1,
    val rewardPoints: Int = 0
)
