package com.Qerxavi7.Nomlut.presentation.collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Qerxavi7.Nomlut.core.ui.AppIcons
import com.Qerxavi7.Nomlut.R
import com.Qerxavi7.Nomlut.core.di.ServiceLocator
import com.Qerxavi7.Nomlut.core.theme.Accent
import com.Qerxavi7.Nomlut.core.theme.GlassFill
import com.Qerxavi7.Nomlut.core.theme.SecondaryRose
import com.Qerxavi7.Nomlut.core.theme.TextPrimary
import com.Qerxavi7.Nomlut.core.ui.GlassCard
import com.Qerxavi7.Nomlut.core.ui.ScreenBackdrop
import com.Qerxavi7.Nomlut.core.ui.ScreenHeader
import com.Qerxavi7.Nomlut.core.ui.SecondaryButton
import androidx.compose.ui.graphics.Color

@Composable
fun CollectionScreen(onMenu: () -> Unit) {
    val collectionViewModel: CollectionViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by collectionViewModel.state.collectAsStateWithLifecycle()

    CollectionContent(state = state, onMenu = onMenu)
}

@Composable
private fun CollectionContent(
    state: CollectionUiState,
    onMenu: () -> Unit
) {
    ScreenBackdrop(
        overlay = listOf(Color(0xE6101827), Color(0xF2141D2C))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.collection),
                backLabel = stringResource(R.string.cd_back),
                onBack = onMenu,
                trailing = {
                    Text(
                        text = stringResource(
                            R.string.collection_counter,
                            state.unlockedCount,
                            state.totalCount
                        ),
                        style = MaterialTheme.typography.titleSmall,
                        color = Accent
                    )
                }
            )

            if (state.empty) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.no_relics_yet),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextPrimary.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.entries) { entry ->
                    RelicCard(entry = entry)
                }
            }

            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 14.dp)
            )
        }
    }
}

@Composable
private fun RelicCard(entry: RelicEntry) {
    val stateText = stringResource(if (entry.unlocked) R.string.unlocked else R.string.locked)
    GlassCard(
        modifier = Modifier.semantics { stateDescription = stateText },
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(92.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(entry.relic.spriteRes),
                contentDescription = stringResource(entry.relic.nameRes),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(86.dp)
                    .alpha(if (entry.unlocked) 1f else 0.22f)
            )
            if (!entry.unlocked) {
                Icon(
                    imageVector = AppIcons.Lock,
                    contentDescription = null,
                    tint = TextPrimary.copy(alpha = 0.75f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(entry.relic.nameRes),
            style = MaterialTheme.typography.titleSmall,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = stringResource(R.string.clear_stage, entry.relic.unlockStage),
            style = MaterialTheme.typography.labelSmall,
            color = SecondaryRose.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(GlassFill)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(entry.relic.rarity / 5f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Accent.copy(alpha = if (entry.unlocked) 0.9f else 0.3f))
            )
        }
    }
}
