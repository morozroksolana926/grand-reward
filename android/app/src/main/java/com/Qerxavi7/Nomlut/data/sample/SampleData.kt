package com.Qerxavi7.Nomlut.data.sample

import com.Qerxavi7.Nomlut.R
import com.Qerxavi7.Nomlut.core.config.GameConfig
import com.Qerxavi7.Nomlut.domain.model.DailyGoal
import com.Qerxavi7.Nomlut.domain.model.GoalKind
import com.Qerxavi7.Nomlut.domain.model.Relic
import com.Qerxavi7.Nomlut.domain.model.StageDef

object SampleData {

    private val chamberNames = listOf(
        R.string.stage_1,
        R.string.stage_2,
        R.string.stage_3,
        R.string.stage_4,
        R.string.stage_5,
        R.string.stage_6,
        R.string.stage_7,
        R.string.stage_8,
        R.string.stage_9,
        R.string.stage_10,
        R.string.stage_11,
        R.string.stage_12
    )

    val stages: List<StageDef> = chamberNames.mapIndexed { position, nameRes ->
        StageDef(
            index = position + 1,
            nameRes = nameRes,
            target = GameConfig.BASE_TARGET + position * GameConfig.TARGET_STEP,
            moves = GameConfig.MOVES_PER_STAGE
        )
    }

    val relics: List<Relic> = listOf(
        Relic(1, R.string.relic_1, R.drawable.sprite_1, 1, 1),
        Relic(2, R.string.relic_2, R.drawable.sprite_2, 3, 2),
        Relic(3, R.string.relic_3, R.drawable.sprite_3, 5, 3),
        Relic(4, R.string.relic_4, R.drawable.sprite_4, 8, 4),
        Relic(5, R.string.relic_5, R.drawable.sprite_5, 12, 5)
    )

    val dailyGoals: List<DailyGoal> = listOf(
        DailyGoal(GoalKind.TOKENS_CLEARED, R.string.goal_tokens, 40, 150),
        DailyGoal(GoalKind.SINGLE_STAGE_SCORE, R.string.goal_score, 2500, 250),
        DailyGoal(GoalKind.STAGES_COMPLETED, R.string.goal_stages, 2, 400)
    )

    fun stageFor(index: Int): StageDef =
        stages.getOrElse(index - 1) { stages[stages.size - 1] }

    fun relicForStage(stage: Int): Relic? = relics.firstOrNull { it.unlockStage == stage }

    fun nearestRelic(stage: Int): Relic =
        relics.lastOrNull { it.unlockStage <= stage } ?: relics[0]
}
