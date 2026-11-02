package com.Qerxavi7.Nomlut.presentation.collection

import com.Qerxavi7.Nomlut.domain.model.Relic

data class RelicEntry(
    val relic: Relic,
    val unlocked: Boolean
)

data class CollectionUiState(
    val entries: List<RelicEntry> = emptyList(),
    val unlockedCount: Int = 0,
    val totalCount: Int = 0
) {
    val empty: Boolean get() = unlockedCount == 0
}
