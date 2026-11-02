package com.Qerxavi7.Nomlut.presentation.quests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Qerxavi7.Nomlut.data.sample.SampleData
import com.Qerxavi7.Nomlut.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class QuestsViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(QuestsUiState())
    val state: StateFlow<QuestsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(progressRepository.profile, progressRepository.goals) { profile, goals ->
                QuestsUiState(
                    goals = goals,
                    nodes = SampleData.stages.map { stage ->
                        val status = when {
                            stage.index < profile.stage -> StageStatus.CLEARED
                            stage.index == profile.stage -> StageStatus.CURRENT
                            else -> StageStatus.LOCKED
                        }
                        StageNode(stage, status)
                    },
                    currentStage = profile.stage,
                    rewardPoints = profile.rewardPoints
                )
            }.collect { next ->
                _state.value = next
            }
        }
    }
}
