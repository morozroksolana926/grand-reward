package com.Qerxavi7.Nomlut.presentation.quests

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Qerxavi7.Nomlut.core.ui.AppIcons
import com.Qerxavi7.Nomlut.R
import com.Qerxavi7.Nomlut.core.di.ServiceLocator
import com.Qerxavi7.Nomlut.core.theme.Accent
import com.Qerxavi7.Nomlut.core.theme.GlassFill
import com.Qerxavi7.Nomlut.core.theme.Primary
import com.Qerxavi7.Nomlut.core.theme.SecondaryRose
import com.Qerxavi7.Nomlut.core.theme.TextMuted
import com.Qerxavi7.Nomlut.core.theme.TextPrimary
import com.Qerxavi7.Nomlut.core.theme.TextSecondary
import com.Qerxavi7.Nomlut.core.ui.GlassCard
import com.Qerxavi7.Nomlut.core.ui.PrimaryButton
import com.Qerxavi7.Nomlut.core.ui.ScreenBackdrop
import com.Qerxavi7.Nomlut.core.ui.ScreenHeader
import com.Qerxavi7.Nomlut.core.ui.SecondaryButton
import com.Qerxavi7.Nomlut.core.ui.formatPoints
import com.Qerxavi7.Nomlut.domain.model.DailyGoalProgress

@Composable
fun QuestsScreen(
    onPlay: () -> Unit,
    onMenu: () -> Unit
) {
    val questsViewModel: QuestsViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by questsViewModel.state.collectAsStateWithLifecycle()

    QuestsContent(state = state, onPlay = onPlay, onMenu = onMenu)
}

@Composable
private fun QuestsContent(
    state: QuestsUiState,
    onPlay: () -> Unit,
    onMenu: () -> Unit
) {
    ScreenBackdrop(
        overlay = listOf(Color(0xE6101827), Color(0xF21A2435))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.quests),
                backLabel = stringResource(R.string.cd_back),
                onBack = onMenu,
                trailing = {
                    Text(
                        text = formatPoints(state.rewardPoints),
                        style = MaterialTheme.typography.titleSmall,
                        color = Accent
                    )
                }
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    SectionTitle(text = stringResource(R.string.daily_goals))
                }
                items(state.goals) { goal ->
                    GoalRow(goal = goal)
                }
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    SectionTitle(text = stringResource(R.string.progression_track))
                }
                items(state.nodes) { node ->
                    StageRow(node = node)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                PrimaryButton(
                    text = stringResource(R.string.play),
                    onClick = onPlay,
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
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = Accent.copy(alpha = 0.85f),
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
private fun GoalRow(goal: DailyGoalProgress) {
    val stateText = stringResource(
        if (goal.completed) R.string.completed else R.string.in_progress
    )
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { stateDescription = stateText },
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (goal.completed) {
                    Icon(
                        imageVector = AppIcons.CheckCircle,
                        contentDescription = null,
                        tint = Accent,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .border(2.dp, TextMuted.copy(alpha = 0.6f), CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(goal.goal.titleRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
            }
            Text(
                text = stringResource(R.string.goal_reward, goal.goal.reward),
                style = MaterialTheme.typography.labelMedium,
                color = Accent
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(GlassFill)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(goal.fraction)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Brush.horizontalGradient(listOf(Primary, Accent)))
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.goal_progress, goal.progress, goal.goal.target),
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun StageRow(node: StageNode) {
    val badgeColor = when (node.status) {
        StageStatus.CLEARED -> Accent
        StageStatus.CURRENT -> SecondaryRose
        StageStatus.LOCKED -> TextMuted
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(GlassFill)
            .border(1.dp, badgeColor.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        if (node.status == StageStatus.CLEARED) {
                            badgeColor.copy(alpha = 0.85f)
                        } else {
                            Color.Transparent
                        }
                    )
                    .border(2.dp, badgeColor.copy(alpha = 0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = formatPoints(node.stage.index),
                    style = MaterialTheme.typography.titleSmall,
                    color = if (node.status == StageStatus.CLEARED) {
                        Color(0xFF101827)
                    } else {
                        badgeColor
                    }
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = stringResource(node.stage.nameRes),
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary
                )
                Text(
                    text = stringResource(R.string.chamber_target, formatPoints(node.stage.target)),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary.copy(alpha = 0.65f)
                )
            }
        }
    }
}
