package com.Qerxavi7.Nomlut.domain.repository

import com.Qerxavi7.Nomlut.domain.model.DailyGoalProgress
import com.Qerxavi7.Nomlut.domain.model.GameResult
import com.Qerxavi7.Nomlut.domain.model.PlayerProfile
import kotlinx.coroutines.flow.StateFlow

interface ProgressRepository {

    val profile: StateFlow<PlayerProfile>

    val goals: StateFlow<List<DailyGoalProgress>>

    fun applyResult(result: GameResult)

    fun resetProgress()
}
