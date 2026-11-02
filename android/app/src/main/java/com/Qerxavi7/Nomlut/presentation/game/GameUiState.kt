package com.Qerxavi7.Nomlut.presentation.game

import com.Qerxavi7.Nomlut.core.config.GameConfig
import com.Qerxavi7.Nomlut.domain.model.BoardState
import com.Qerxavi7.Nomlut.domain.model.GameResult

data class GameUiState(
    val loading: Boolean = true,
    val board: BoardState = BoardState.Empty,
    val stage: Int = 1,
    val stageNameRes: Int = 0,
    val movesLeft: Int = GameConfig.MOVES_PER_STAGE,
    val score: Int = 0,
    val target: Int = GameConfig.BASE_TARGET,
    val tokensCleared: Int = 0,
    val bestGroup: Int = 0,
    val chainIndex: Int = 0,
    val chainMultiplier: Float = 1f,
    val lastGain: Int = 0,
    val highlighted: Set<Int> = emptySet(),
    val hintGroup: Set<Int> = emptySet(),
    val invalidIndex: Int = -1,
    val inputLocked: Boolean = false,
    val hintUsed: Boolean = false,
    val targetReached: Boolean = false,
    val reshuffled: Boolean = false,
    val paused: Boolean = false,
    val reducedMotion: Boolean = false
) {
    val meterProgress: Float
        get() = if (target <= 0) 0f else (score.toFloat() / target).coerceIn(0f, 1f)

    val meterPercent: Int get() = (meterProgress * 100f).toInt()

    val chainVisible: Boolean get() = chainIndex > 0 && chainMultiplier > 1f

    val boardReady: Boolean get() = !loading && board.cols > 0 && board.cells.isNotEmpty()
}

sealed interface GameEvent {
    data class Finished(val result: GameResult) : GameEvent
}
