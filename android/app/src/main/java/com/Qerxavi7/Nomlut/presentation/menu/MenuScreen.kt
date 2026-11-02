package com.Qerxavi7.Nomlut.presentation.menu

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
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
import com.Qerxavi7.Nomlut.core.ui.DecorParticles
import com.Qerxavi7.Nomlut.core.ui.PrimaryButton
import com.Qerxavi7.Nomlut.core.ui.ScreenBackdrop
import com.Qerxavi7.Nomlut.core.ui.SecondaryButton
import com.Qerxavi7.Nomlut.core.ui.StatCard
import com.Qerxavi7.Nomlut.core.ui.formatPoints
import com.Qerxavi7.Nomlut.presentation.dialog.ConfirmResetDialog
import com.Qerxavi7.Nomlut.presentation.dialog.SettingsDialog

@Composable
fun MenuScreen(
    onPlay: () -> Unit,
    onCollection: () -> Unit,
    onQuests: () -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by menuViewModel.state.collectAsStateWithLifecycle()

    MenuContent(
        state = state,
        onPlay = onPlay,
        onCollection = onCollection,
        onQuests = onQuests,
        onSettings = menuViewModel::openSettings
    )

    if (state.settingsVisible) {
        SettingsDialog(
            soundCues = state.soundCues,
            haptics = state.haptics,
            reducedMotion = state.reducedMotion,
            onSoundCues = menuViewModel::setSoundCues,
            onHaptics = menuViewModel::setHaptics,
            onReducedMotion = menuViewModel::setReducedMotion,
            onResetRequest = menuViewModel::requestReset,
            onDismiss = menuViewModel::closeSettings
        )
    }
    if (state.confirmResetVisible) {
        ConfirmResetDialog(
            onConfirm = menuViewModel::confirmReset,
            onCancel = menuViewModel::cancelReset
        )
    }
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onPlay: () -> Unit,
    onCollection: () -> Unit,
    onQuests: () -> Unit,
    onSettings: () -> Unit
) {
    ScreenBackdrop(
        overlay = listOf(
            Color(0x33101827),
            Color(0x99101827),
            Color(0xF2101827)
        )
    ) {
        DecorParticles(
            count = 10,
            animated = !state.reducedMotion,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(GlassFill)
                        .border(1.dp, Accent.copy(alpha = 0.24f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon_home),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0x1AFFF8E7))
                        .border(1.dp, Accent.copy(alpha = 0.24f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp)
                        .semantics { liveRegion = LiveRegionMode.Polite },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = AppIcons.Star,
                        contentDescription = null,
                        tint = Accent,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.rp_value, formatPoints(state.rewardPoints)),
                        style = MaterialTheme.typography.titleSmall,
                        color = Accent
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                FloatingRelic(
                    spriteRes = R.drawable.sprite_1,
                    label = stringResource(R.string.token_gold_ingot),
                    size = 104.dp,
                    periodMs = 3200,
                    animated = !state.reducedMotion,
                    modifier = Modifier.offset(x = 0.dp, y = (-18).dp)
                )
                FloatingRelic(
                    spriteRes = R.drawable.sprite_2,
                    label = stringResource(R.string.token_ruby_seal),
                    size = 72.dp,
                    periodMs = 3800,
                    animated = !state.reducedMotion,
                    modifier = Modifier.offset(x = (-104).dp, y = 28.dp)
                )
                FloatingRelic(
                    spriteRes = R.drawable.sprite_3,
                    label = stringResource(R.string.token_rose_gem),
                    size = 72.dp,
                    periodMs = 4400,
                    animated = !state.reducedMotion,
                    modifier = Modifier.offset(x = 104.dp, y = 40.dp)
                )
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                color = SheetColor,
                border = BorderStroke(1.dp, Accent.copy(alpha = 0.24f)),
                shadowElevation = 18.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp, vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .width(44.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Accent.copy(alpha = 0.35f))
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.splash_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.menu_tagline),
                        style = MaterialTheme.typography.bodySmall,
                        letterSpacing = 1.5.sp,
                        color = SecondaryRose.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (state.hasRecord) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatCard(
                                label = stringResource(R.string.stat_best_score),
                                value = formatPoints(state.bestScore),
                                valueColor = Accent,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                label = stringResource(R.string.stat_stage),
                                value = formatPoints(state.stage),
                                valueColor = SecondaryRose,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    } else {
                        StatCard(
                            label = stringResource(R.string.stat_stage),
                            value = formatPoints(state.stage),
                            valueColor = Accent,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.menu_hint),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextPrimary.copy(alpha = 0.55f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    PrimaryButton(
                        text = stringResource(R.string.play),
                        onClick = onPlay,
                        leadingIcon = AppIcons.PlayArrow,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SecondaryButton(
                            text = stringResource(R.string.collection),
                            onClick = onCollection,
                            modifier = Modifier.weight(1f)
                        )
                        SecondaryButton(
                            text = stringResource(R.string.quests),
                            onClick = onQuests,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = onSettings,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(GlassFill)
                                .border(1.dp, Accent.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                        ) {
                            Icon(
                                imageVector = AppIcons.Settings,
                                contentDescription = stringResource(R.string.settings),
                                tint = Accent,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FloatingRelic(
    spriteRes: Int,
    label: String,
    size: Dp,
    periodMs: Int,
    animated: Boolean,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "relicFloat")
    val drift by transition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = periodMs),
            repeatMode = RepeatMode.Reverse
        ),
        label = "relicDrift"
    )
    val offsetY = if (animated) drift.dp else 0.dp
    Image(
        painter = painterResource(spriteRes),
        contentDescription = label,
        contentScale = ContentScale.Fit,
        modifier = modifier
            .offset(y = offsetY)
            .size(size)
    )
}
