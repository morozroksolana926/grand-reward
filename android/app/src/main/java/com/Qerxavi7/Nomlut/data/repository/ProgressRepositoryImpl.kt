package com.Qerxavi7.Nomlut.data.repository

import com.Qerxavi7.Nomlut.core.config.GameConfig
import com.Qerxavi7.Nomlut.data.local.PreferencesStorage
import com.Qerxavi7.Nomlut.data.sample.SampleData
import com.Qerxavi7.Nomlut.domain.model.DailyGoalProgress
import com.Qerxavi7.Nomlut.domain.model.GameResult
import com.Qerxavi7.Nomlut.domain.model.GoalKind
import com.Qerxavi7.Nomlut.domain.model.PlayerProfile
import com.Qerxavi7.Nomlut.domain.repository.ProgressRepository
import com.Qerxavi7.Nomlut.domain.usecase.UpdateDailyGoalsUseCase
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressRepositoryImpl(
    private val storage: PreferencesStorage
) : ProgressRepository {

    private val updateDailyGoals = UpdateDailyGoalsUseCase()

    private val _profile = MutableStateFlow(readProfile())
    override val profile: StateFlow<PlayerProfile> = _profile.asStateFlow()

    private val _goals = MutableStateFlow(readGoals())
    override val goals: StateFlow<List<DailyGoalProgress>> = _goals.asStateFlow()

    override fun applyResult(result: GameResult) {
        val current = _profile.value
        val unlocked = current.unlockedRelics.toMutableSet()
        if (result.stageCleared) {
            val relic = SampleData.relicForStage(result.stage)
            if (relic != null) unlocked.add(relic.id)
        }
        val nextStage = if (result.stageCleared) {
            minOf(result.stage + 1, GameConfig.TOTAL_STAGES)
        } else {
            result.stage
        }
        val updated = current.copy(
            rewardPoints = current.rewardPoints + result.rewardPoints,
            bestScore = maxOf(current.bestScore, result.score),
            stage = nextStage,
            stagesCleared = current.stagesCleared + if (result.stageCleared) 1 else 0,
            tokensCleared = current.tokensCleared + result.tokensCleared,
            unlockedRelics = unlocked
        )
        writeProfile(updated)
        _profile.value = updated

        val currentGoals = _goals.value.associate { it.goal.kind to it.progress }
        val nextGoals = updateDailyGoals(currentGoals, result)
        storage.writeLong(KEY_GOAL_DAY, currentEpochDay())
        for (entry in nextGoals) {
            storage.writeInt(goalKey(entry.key), entry.value)
        }
        _goals.value = SampleData.dailyGoals.map { goal ->
            DailyGoalProgress(goal, nextGoals[goal.kind] ?: 0)
        }
    }

    override fun resetProgress() {
        val today = currentEpochDay()
        for (goal in SampleData.dailyGoals) {
            storage.writeInt(goalKey(goal.kind), 0)
        }
        storage.writeLong(KEY_GOAL_DAY, today)
        val fresh = PlayerProfile()
        writeProfile(fresh)
        _profile.value = fresh
        _goals.value = SampleData.dailyGoals.map { DailyGoalProgress(it, 0) }
    }

    private fun readProfile(): PlayerProfile = PlayerProfile(
        rewardPoints = storage.readInt(KEY_POINTS, 0),
        bestScore = storage.readInt(KEY_BEST, 0),
        stage = storage.readInt(KEY_STAGE, 1).coerceIn(1, GameConfig.TOTAL_STAGES),
        stagesCleared = storage.readInt(KEY_CLEARED, 0),
        tokensCleared = storage.readInt(KEY_TOKENS, 0),
        unlockedRelics = storage.readIntSet(KEY_RELICS)
    )

    private fun writeProfile(value: PlayerProfile) {
        storage.writeInt(KEY_POINTS, value.rewardPoints)
        storage.writeInt(KEY_BEST, value.bestScore)
        storage.writeInt(KEY_STAGE, value.stage)
        storage.writeInt(KEY_CLEARED, value.stagesCleared)
        storage.writeInt(KEY_TOKENS, value.tokensCleared)
        storage.writeIntSet(KEY_RELICS, value.unlockedRelics)
    }

    private fun readGoals(): List<DailyGoalProgress> {
        val today = currentEpochDay()
        if (storage.readLong(KEY_GOAL_DAY, 0L) != today) {
            storage.writeLong(KEY_GOAL_DAY, today)
            for (goal in SampleData.dailyGoals) {
                storage.writeInt(goalKey(goal.kind), 0)
            }
        }
        return SampleData.dailyGoals.map { goal ->
            DailyGoalProgress(goal, storage.readInt(goalKey(goal.kind), 0))
        }
    }

    private fun currentEpochDay(): Long = LocalDate.now().toEpochDay()

    private fun goalKey(kind: GoalKind): String = KEY_GOAL_PREFIX + kind.name

    private companion object {
        const val KEY_POINTS = "reward_points"
        const val KEY_BEST = "best_score"
        const val KEY_STAGE = "current_stage"
        const val KEY_CLEARED = "stages_cleared"
        const val KEY_TOKENS = "tokens_cleared"
        const val KEY_RELICS = "unlocked_relics"
        const val KEY_GOAL_DAY = "goal_epoch_day"
        const val KEY_GOAL_PREFIX = "goal_"
    }
}
