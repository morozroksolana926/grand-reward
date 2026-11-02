package com.Qerxavi7.Nomlut.presentation.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Qerxavi7.Nomlut.core.ui.AppIcons
import com.Qerxavi7.Nomlut.R
import com.Qerxavi7.Nomlut.core.di.ServiceLocator
import com.Qerxavi7.Nomlut.core.theme.Accent
import com.Qerxavi7.Nomlut.core.theme.GlassFill
import com.Qerxavi7.Nomlut.core.theme.SecondaryRose
import com.Qerxavi7.Nomlut.core.theme.SheetColor
import com.Qerxavi7.Nomlut.core.theme.TextPrimary
import com.Qerxavi7.Nomlut.core.theme.TextSecondary
import com.Qerxavi7.Nomlut.core.ui.PrimaryButton
import com.Qerxavi7.Nomlut.core.ui.RewardMeter
import com.Qerxavi7.Nomlut.core.ui.RewardToken
import com.Qerxavi7.Nomlut.core.ui.ScreenBackdrop
import com.Qerxavi7.Nomlut.core.ui.SecondaryButton
import com.Qerxavi7.Nomlut.core.ui.StatPill
import com.Qerxavi7.Nomlut.core.ui.formatMultiplier
import com.Qerxavi7.Nomlut.core.ui.formatPoints
import com.Qerxavi7.Nomlut.core.ui.visualFor
import com.Qerxavi7.Nomlut.domain.model.GameResult
import com.Qerxavi7.Nomlut.presentation.dialog.PauseDialog
import kotlin.math.floor

@Composable
fun GameScreen(
    round: Int,
    onExit: () -> Unit,
    onFinished: (GameResult) -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(round) {
        gameViewModel.prepare(round)
    }

    LaunchedEffect(Unit) {
        gameViewModel.events.collect { event ->
            when (event) {
                is GameEvent.Finished -> onFinished(event.result)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { gameViewModel.pause() }
    }

    GameContent(
        state = state,
        onBack = onExit,
        onPause = gameViewModel::pause,
        onTokenTap = gameViewModel::onTokenTap,
        onShuffle = gameViewModel::onShuffle,
        onHint = gameViewModel::onHint,
        onClaim = gameViewModel::onClaimReward
    )

    if (state.paused) {
        PauseDialog(
            scoreText = formatPoints(state.score),
            moves = state.movesLeft,
            onResume = gameViewModel::resume,
            onRestart = {
                gameViewModel.resume()
                gameViewModel.restartStage()
            },
            onMenu = onExit
        )
    }
}

@Composable
private fun GameContent(
    state: GameUiState,
    onBack: () -> Unit,
    onPause: () -> Unit,
    onTokenTap: (Int) -> Unit,
    onShuffle: () -> Unit,
    onHint: () -> Unit,
    onClaim: () -> Unit
) {
    ScreenBackdrop(
        overlay = listOf(
            Color(0xE6101827),
            Color(0xF21A2435),
            Color(0xFA0C121D)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(
                stage = state.stage,
                stageNameRes = state.stageNameRes,
                onBack = onBack,
                onPause = onPause
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (state.boardReady) {
                    GameBoard(state = state, onTokenTap = onTokenTap)
                } else {
                    Box(
                        modifier = Modifier
                            .size(240.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(GlassFill)
                            .border(2.dp, Accent.copy(alpha = 0.2f), RoundedCornerShape(22.dp))
                    )
                }
                FadePopup(
                    visible = state.lastGain > 0,
                    enter = fadeIn(tween(140)),
                    exit = fadeOut(tween(220)),
                    modifier = Modifier.align(Alignment.TopCenter)
                ) {
                    Text(
                        text = stringResource(R.string.gain_value, state.lastGain),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = Accent
                    )
                }
                FadePopup(
                    visible = state.reshuffled,
                    enter = fadeIn(tween(160)),
                    exit = fadeOut(tween(240)),
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Text(
                        text = stringResource(R.string.board_reshuffled),
                        style = MaterialTheme.typography.labelSmall,
                        color = SecondaryRose
                    )
                }
            }

            StatStrip(state = state)

            Spacer(modifier = Modifier.height(12.dp))

            ControlsBlock(
                state = state,
                onShuffle = onShuffle,
                onHint = onHint,
                onClaim = onClaim
            )
        }
    }
}

@Composable
private fun GameHeader(
    stage: Int,
    stageNameRes: Int,
    onBack: () -> Unit,
    onPause: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0x4D000000))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
            Icon(
                imageVector = AppIcons.ArrowBack,
                contentDescription = stringResource(R.string.cd_back),
                tint = TextPrimary,
                modifier = Modifier.size(24.dp)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.vault_chamber),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                letterSpacing = 3.sp,
                color = Accent.copy(alpha = 0.7f)
            )
            Text(
                text = stringResource(R.string.stage_header, stage),
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
            if (stageNameRes != 0) {
                Text(
                    text = stringResource(stageNameRes),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = TextSecondary.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )
            }
        }
        IconButton(onClick = onPause, modifier = Modifier.size(48.dp)) {
            Image(
                painter = painterResource(R.drawable.icon_pause),
                contentDescription = stringResource(R.string.cd_pause),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Composable
private fun GameBoard(
    state: GameUiState,
    onTokenTap: (Int) -> Unit
) {
    val cols = state.board.cols
    val rows = state.board.rows
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val frame = 8.dp
        val widthBudget = if (maxWidth - 32.dp < 380.dp) maxWidth - 32.dp else 380.dp
        val heightBudget = maxHeight - 24.dp
        val tileByWidth = floor(((widthBudget - frame * 2) / cols).value).dp
        val tileByHeight = floor(((heightBudget - frame * 2) / rows).value).dp
        val smallest = if (tileByWidth < tileByHeight) tileByWidth else tileByHeight
        val tile = if (smallest < 24.dp) 24.dp else smallest
        val boardWidth = tile * cols + frame * 2
        val boardHeight = tile * rows + frame * 2
        val shape = RoundedCornerShape(22.dp)
        Column(
            modifier = Modifier
                .size(width = boardWidth, height = boardHeight)
                .shadow(20.dp, shape, ambientColor = Accent, spotColor = Accent)
                .clip(shape)
                .background(Color(0x0FFFF8E7))
                .border(2.dp, Accent.copy(alpha = 0.32f), shape)
                .padding(frame)
        ) {
            for (row in 0 until rows) {
                Row {
                    for (col in 0 until cols) {
                        val index = row * cols + col
                        val type = state.board.cells.getOrNull(index)
                        if (type == null) {
                            Spacer(modifier = Modifier.size(tile))
                        } else {
                            val visual = visualFor(type)
                            val label = stringResource(visual.labelRes)
                            RewardToken(
                                spriteRes = visual.spriteRes,
                                tint = visual.tint,
                                size = tile,
                                label = label,
                                stateText = label,
                                highlighted = index in state.highlighted,
                                hinted = index in state.hintGroup,
                                shaking = index == state.invalidIndex,
                                onClick = { onTokenTap(index) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatStrip(state: GameUiState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(GlassFill)
            .border(1.dp, Accent.copy(alpha = 0.18f), RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatPill(
            label = stringResource(R.string.moves),
            value = formatPoints(state.movesLeft),
            valueColor = SecondaryRose
        )
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(28.dp)
                .background(TextPrimary.copy(alpha = 0.12f))
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }) {
                StatPill(
                    label = stringResource(R.string.score),
                    value = formatPoints(state.score),
                    valueColor = Accent
                )
            }
            if (state.chainVisible) {
                Text(
                    text = stringResource(
                        R.string.chain_value,
                        formatMultiplier(state.chainMultiplier)
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    color = Accent.copy(alpha = 0.8f)
                )
            }
        }
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(28.dp)
                .background(TextPrimary.copy(alpha = 0.12f))
        )
        StatPill(
            label = stringResource(R.string.target),
            value = formatPoints(state.target),
            valueColor = TextPrimary
        )
    }
}

@Composable
private fun ControlsBlock(
    state: GameUiState,
    onShuffle: () -> Unit,
    onHint: () -> Unit,
    onClaim: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
        color = SheetColor,
        border = BorderStroke(1.dp, Accent.copy(alpha = 0.24f)),
        shadowElevation = 16.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            RewardMeter(
                label = stringResource(R.string.grand_reward_meter),
                percentText = stringResource(R.string.meter_percent, state.meterPercent),
                stateText = stringResource(R.string.meter_state, state.meterPercent),
                progress = state.meterProgress,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecondaryButton(
                    text = stringResource(R.string.shuffle),
                    onClick = onShuffle,
                    enabled = !state.inputLocked && state.movesLeft > 1,
                    leadingIcon = AppIcons.Refresh,
                    modifier = Modifier.weight(1f)
                )
                SecondaryButton(
                    text = stringResource(R.string.hint),
                    onClick = onHint,
                    enabled = !state.hintUsed && !state.inputLocked,
                    leadingIcon = AppIcons.Search,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            PrimaryButton(
                text = stringResource(R.string.claim_reward),
                onClick = onClaim,
                enabled = state.targetReached && !state.inputLocked,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun FadePopup(
    visible: Boolean,
    enter: EnterTransition,
    exit: ExitTransition,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = enter,
        exit = exit
    ) {
        content()
    }
}
