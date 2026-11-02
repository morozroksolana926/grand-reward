package com.Qerxavi7.Nomlut.presentation.gameover

import androidx.lifecycle.ViewModel
import com.Qerxavi7.Nomlut.core.config.GameConfig
import com.Qerxavi7.Nomlut.data.sample.SampleData
import com.Qerxavi7.Nomlut.domain.model.GameResult
import com.Qerxavi7.Nomlut.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameOverViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    private var appliedRound = -1

    fun prepare(round: Int, result: GameResult) {
        if (appliedRound == round) return
        appliedRound = round
        progressRepository.applyResult(result)
        val profile = progressRepository.profile.value
        val stageRelic = SampleData.relicForStage(result.stage)
        _state.value = GameOverUiState(
            loaded = true,
            stageCleared = result.stageCleared,
            score = result.score,
            tokensCleared = result.tokensCleared,
            bestGroup = result.bestGroup,
            rewardPointsEarned = result.rewardPoints,
            totalRewardPoints = profile.rewardPoints,
            bestScore = profile.bestScore,
            relic = stageRelic ?: SampleData.nearestRelic(result.stage),
            relicUnlocked = result.stageCleared && stageRelic != null,
            nextStage = profile.stage,
            hasNextStage = result.stageCleared && result.stage < GameConfig.TOTAL_STAGES
        )
    }
}
