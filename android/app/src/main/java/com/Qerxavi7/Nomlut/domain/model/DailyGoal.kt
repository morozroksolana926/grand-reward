package com.Qerxavi7.Nomlut.domain.model

enum class GoalKind {
    TOKENS_CLEARED,
    SINGLE_STAGE_SCORE,
    STAGES_COMPLETED
}

data class DailyGoal(
    val kind: GoalKind,
    val titleRes: Int,
    val target: Int,
    val reward: Int
)

data class DailyGoalProgress(
    val goal: DailyGoal,
    val progress: Int
) {
    val completed: Boolean get() = progress >= goal.target

    val fraction: Float
        get() = if (goal.target <= 0) 0f else (progress.toFloat() / goal.target).coerceIn(0f, 1f)
}
