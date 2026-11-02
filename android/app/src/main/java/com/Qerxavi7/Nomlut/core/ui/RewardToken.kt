package com.Qerxavi7.Nomlut.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RewardToken(
    spriteRes: Int,
    tint: Color,
    size: Dp,
    label: String,
    stateText: String,
    highlighted: Boolean,
    hinted: Boolean,
    shaking: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(14.dp)
    val scale by animateFloatAsState(
        targetValue = if (highlighted) 1.08f else if (hinted) 1.05f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 500f),
        label = "tokenScale"
    )
    val shake by animateFloatAsState(
        targetValue = if (shaking) 4f else 0f,
        animationSpec = tween(durationMillis = 80),
        label = "tokenShake"
    )
    val borderAlpha = if (highlighted || hinted) 0.9f else 0.42f
    Box(
        modifier = modifier
            .size(size)
            .padding(3.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = shake
            }
            .clip(shape)
            .background(tint.copy(alpha = if (highlighted) 0.30f else 0.16f))
            .border(1.dp, tint.copy(alpha = borderAlpha), shape)
            .clickable(onClick = onClick)
            .semantics { stateDescription = stateText },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(spriteRes),
            contentDescription = label,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .padding(7.dp)
        )
    }
}
