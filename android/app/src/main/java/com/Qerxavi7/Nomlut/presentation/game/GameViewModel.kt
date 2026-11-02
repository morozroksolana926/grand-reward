package com.Qerxavi7.Nomlut.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Qerxavi7.Nomlut.core.config.GameConfig
import com.Qerxavi7.Nomlut.data.sample.SampleData
import com.Qerxavi7.Nomlut.domain.model.BoardState
import com.Qerxavi7.Nomlut.domain.model.GameResult
import com.Qerxavi7.Nomlut.domain.repository.ProgressRepository
import com.Qerxavi7.Nomlut.domain.repository.SettingsRepository
import com.Qerxavi7.Nomlut.domain.usecase.CollapseGroupUseCase
import com.Qerxavi7.Nomlut.domain.usecase.ResolveBoardUseCase
import com.Qerxavi7.Nomlut.domain.usecase.ScoreGroupUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val findGroup = CollapseGroupUseCase()
    private val resolveBoard = ResolveBoardUseCase()
    private val scoreGroup = ScoreGroupUseCase()
    private val random = Random.Default

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val eventChannel = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = eventChannel.receiveAsFlow()

    private var activeRound = -1
    private var finished = false
    private var lastCollapseAt = 0L
    private var cascadeJob: Job? = null

    fun prepare(round: Int) {
        if (activeRound == round) return
        activeRound = round
        loadStage()
    }

    fun restartStage() {
        loadStage()
    }

    private fun loadStage() {
        cascadeJob?.cancel()
        finished = false
        lastCollapseAt = 0L
        val stage = progressRepository.profile.value.stage.coerceIn(1, GameConfig.TOTAL_STAGES)
        val definition = SampleData.stageFor(stage)
        val reducedMotion = settingsRepository.settings.value.reducedMotion
        _state.value = GameUiState(
            loading = true,
            stage = stage,
            stageNameRes = definition.nameRes,
            movesLeft = definition.moves,
            target = definition.target,
            reducedMotion = reducedMotion
        )
        cascadeJob = viewModelScope.launch {
            val board = buildPlayableBoard()
            delay(GameConfig.BOARD_BUILD_MS)
            _state.update { it.copy(loading = false, board = board) }
        }
    }

    fun onTokenTap(index: Int) {
        val current = _state.value
        if (finished || current.loading || current.inputLocked || current.paused) return
        val board = current.board
        if (index < 0 || index >= board.cells.size) return
        val group = findGroup(board, index % board.cols, index / board.cols)
        if (group.size < GameConfig.MIN_GROUP) {
            viewModelScope.launch {
                _state.update { it.copy(invalidIndex = index) }
                delay(GameConfig.INVALID_SHAKE_MS)
                _state.update { it.copy(invalidIndex = -1) }
            }
            return
        }
        cascadeJob = viewModelScope.launch { runCascade(group) }
    }

    fun onShuffle() {
        val current = _state.value
        if (finished || current.loading || current.inputLocked || current.movesLeft <= 0) return
        cascadeJob = viewModelScope.launch {
            _state.update { it.copy(inputLocked = true, hintGroup = emptySet()) }
            var board = resolveBoard.shuffle(current.board, random)
            var attempts = 0
            while (!resolveBoard.hasMoves(board) && attempts < MAX_SHUFFLES) {
                board = resolveBoard.shuffle(board, random)
                attempts += 1
            }
            delay(GameConfig.RESHUFFLE_MS)
            _state.update {
                it.copy(
                    board = board,
                    movesLeft = it.movesLeft - 1,
                    inputLocked = false,
                    reshuffled = true,
                    chainIndex = 0,
                    chainMultiplier = 1f
                )
            }
            delay(GameConfig.HINT_PULSE_MS)
            _state.update { it.copy(reshuffled = false) }
            val after = _state.value
            if (after.movesLeft <= 0) finishRound(after.targetReached)
        }
    }

    fun onHint() {
        val current = _state.value
        if (finished || current.loading || current.inputLocked || current.hintUsed) return
        val best = findGroup.largestGroup(current.board)
        if (best.isEmpty()) return
        viewModelScope.launch {
            _state.update { it.copy(hintGroup = best, hintUsed = true) }
            delay(GameConfig.HINT_PULSE_MS)
            _state.update { it.copy(hintGroup = emptySet()) }
        }
    }

    fun onClaimReward() {
        val current = _state.value
        if (finished || !current.targetReached || current.inputLocked) return
        finishRound(true)
    }

    fun pause() {
        if (finished) return
        _state.update { it.copy(paused = true) }
    }

    fun resume() {
        _state.update { it.copy(paused = false) }
    }

    private suspend fun runCascade(group: Set<Int>) {
        val now = System.currentTimeMillis()
        val chain = if (lastCollapseAt > 0L && now - lastCollapseAt <= GameConfig.CHAIN_WINDOW_MS) {
            _state.value.chainIndex + 1
        } else {
            0
        }
        lastCollapseAt = now
        val gained = scoreGroup(group.size, chain)
        _state.update {
            it.copy(
                highlighted = group,
                hintGroup = emptySet(),
                inputLocked = true,
                chainIndex = chain,
                chainMultiplier = scoreGroup.multiplier(chain),
                lastGain = gained
            )
        }
        delay(GameConfig.COLLAPSE_HIGHLIGHT_MS)

        val cleared = resolveBoard.clear(_state.value.board, group)
        _state.update { it.copy(board = cleared, highlighted = emptySet()) }
        delay(GameConfig.COLLAPSE_FADE_MS)

        var settled = resolveBoard(cleared, random)
        var reshuffled = false
        var attempts = 0
        while (!resolveBoard.hasMoves(settled) && attempts < MAX_SHUFFLES) {
            settled = resolveBoard.shuffle(settled, random)
            reshuffled = true
            attempts += 1
        }
        _state.update {
            val score = it.score + gained
            it.copy(
                board = settled,
                score = score,
                movesLeft = it.movesLeft - 1,
                tokensCleared = it.tokensCleared + group.size,
                bestGroup = maxOf(it.bestGroup, group.size),
                targetReached = score >= it.target,
                reshuffled = reshuffled,
                inputLocked = false
            )
        }
        delay(GameConfig.REFILL_MS)
        _state.update { it.copy(lastGain = 0, reshuffled = false) }

        val after = _state.value
        if (after.movesLeft <= 0) finishRound(after.targetReached)
    }

    private suspend fun buildPlayableBoard(): BoardState {
        var board = resolveBoard.create(GameConfig.COLS, GameConfig.ROWS, random)
        var attempts = 0
        while (!resolveBoard.hasMoves(board) && attempts < MAX_SHUFFLES) {
            board = resolveBoard.create(GameConfig.COLS, GameConfig.ROWS, random)
            attempts += 1
        }
        return board
    }

    private fun finishRound(cleared: Boolean) {
        if (finished) return
        finished = true
        val current = _state.value
        val bonus = if (cleared) current.movesLeft * GameConfig.CLEAR_BONUS_PER_MOVE else 0
        val result = GameResult(
            stage = current.stage,
            score = current.score + bonus,
            tokensCleared = current.tokensCleared,
            bestGroup = current.bestGroup,
            movesLeft = current.movesLeft,
            stageCleared = cleared
        )
        _state.update { it.copy(inputLocked = true, paused = false) }
        viewModelScope.launch { eventChannel.send(GameEvent.Finished(result)) }
    }

    private companion object {
        const val MAX_SHUFFLES = 24
    }
}
