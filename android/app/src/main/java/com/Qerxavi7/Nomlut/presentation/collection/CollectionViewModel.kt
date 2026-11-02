package com.Qerxavi7.Nomlut.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Qerxavi7.Nomlut.data.sample.SampleData
import com.Qerxavi7.Nomlut.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CollectionViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CollectionUiState())
    val state: StateFlow<CollectionUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.profile.collect { profile ->
                val entries = SampleData.relics.map { relic ->
                    RelicEntry(relic, profile.unlockedRelics.contains(relic.id))
                }
                _state.value = CollectionUiState(
                    entries = entries,
                    unlockedCount = entries.count { it.unlocked },
                    totalCount = entries.size
                )
            }
        }
    }
}
