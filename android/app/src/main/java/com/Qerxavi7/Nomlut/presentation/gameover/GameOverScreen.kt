package com.Qerxavi7.Nomlut.presentation.gameover

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.Qerxavi7.Nomlut.R
import com.Qerxavi7.Nomlut.core.config.GameConfig
import com.Qerxavi7.Nomlut.core.di.ServiceLocator
import com.Qerxavi7.Nomlut.core.theme.Accent
import com.Qerxavi7.Nomlut.core.theme.GlassFill
import com.Qerxavi7.Nomlut.core.theme.SecondaryRose
import com.Qerxavi7.Nomlut.core.theme.TextPrimary
import com.Qerxavi7.Nomlut.core.ui.PrimaryButton
import com.Qerxavi7.Nomlut.core.ui.ScreenBackdrop
import com.Qerxavi7.Nomlut.core.ui.SecondaryButton
import com.Qerxavi7.Nomlut.core.ui.StatCard
import com.Qerxavi7.Nomlut.core.ui.formatPoints
import com.Qerxavi7.Nomlut.domain.model.GameResult

@Composable
fun GameOverScreen(
    round: Int,
    result: GameResult,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(round, result) {
        gameOverViewModel.prepare(round, result)
    }

    GameOverContent(
        state = state,
        onPlayAgain = onPlayAgain,
        onMenu = onMenu
    )
}

@Composable
private fun GameOverContent(
    state: GameOverUiState,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val cleared = state.stageCleared
    val bannerColor = if (cleared) Accent else SecondaryRose
    val animatedScore by animateIntAsState(
        targetValue = state.score,
        animationSpec = tween(durationMillis = GameConfig.SCORE_COUNTUP_MS),
        label = "scoreCountUp"
    )
    val transition = rememberInfiniteTransition(label = "relicRing")
    val ringSpin by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 22000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ringSpin"
    )

    ScreenBackdrop(
        overlay = if (cleared) {
            listOf(Color(0xCC2A1E12), Color(0xF2101827))
        } else {
            listOf(Color(0xCC2A1218), Color(0xF2101827))
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(width = 300.dp, height = 100.dp)) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(bannerColor.copy(alpha = 0.18f), Color.Transparent),
                            radius = size.maxDimension / 2f
                        ),
                        radius = size.maxDimension / 2f
                    )
                }
                Text(
                    text = stringResource(
                        if (cleared) R.string.vault_cleared else R.string.vault_sealed
                    ),
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 30.sp,
                    letterSpacing = 3.sp,
                    color = bannerColor,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Box(
                modifier = Modifier.size(140.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .size(140.dp)
                        .rotate(ringSpin)
                ) {
                    drawArc(
                        color = Accent.copy(alpha = 0.45f),
                        startAngle = 0f,
                        sweepAngle = 250f,
                        useCenter = false,
                        style = Stroke(width = 2f * density)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(118.dp)
                        .clip(CircleShape)
                        .background(GlassFill)
                        .border(1.dp, Accent.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    val relic = state.relic
                    if (relic != null) {
                        Image(
                            painter = painterResource(relic.spriteRes),
                            contentDescription = stringResource(R.string.cd_relic),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(90.dp)
                                .alpha(if (state.relicUnlocked) 1f else 0.3f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(if (state.relicUnlocked) R.string.unlocked else R.string.locked),
                style = MaterialTheme.typography.labelSmall,
                color = TextPrimary.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.score),
                style = MaterialTheme.typography.labelSmall,
                color = TextPrimary.copy(alpha = 0.6f)
            )
            Text(
                text = formatPoints(animatedScore),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Black,
                color = Accent,
                textAlign = TextAlign.Center,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ResultStats(state = state)

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = stringResource(R.string.gameover_hint),
                style = MaterialTheme.typography.labelSmall,
                color = TextPrimary.copy(alpha = 0.55f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            if (state.hasNextStage) {
                PrimaryButton(
                    text = stringResource(R.string.next_stage),
                    onClick = onPlayAgain,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                SecondaryButton(
                    text = stringResource(R.string.play_again),
                    onClick = onPlayAgain,
                    modifier = Modifier.fillMaxWidth()
                )
                TextButton(onClick = onMenu, modifier = Modifier.height(48.dp)) {
                    Text(
                        text = stringResource(R.string.menu),
                        style = MaterialTheme.typography.labelLarge,
                        color = TextPrimary
                    )
                }
            } else {
                PrimaryButton(
                    text = stringResource(R.string.play_again),
                    onClick = onPlayAgain,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                SecondaryButton(
                    text = stringResource(R.string.menu),
                    onClick = onMenu,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun ResultStats(state: GameOverUiState) {
    val tokensLabel = stringResource(R.string.stat_tokens)
    val groupLabel = stringResource(R.string.stat_best_group)
    val rpLabel = stringResource(R.string.stat_rp_earned)
    val entries = buildList {
        if (state.tokensCleared > 0) add(Triple(tokensLabel, formatPoints(state.tokensCleared), TextPrimary))
        if (state.bestGroup > 0) add(Triple(groupLabel, formatPoints(state.bestGroup), SecondaryRose))
        if (state.rewardPointsEarned > 0) add(Triple(rpLabel, formatPoints(state.rewardPointsEarned), Accent))
    }
    if (entries.size < 2) return
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        entries.forEach { entry ->
            StatCard(
                label = entry.first,
                value = entry.second,
                valueColor = entry.third,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
