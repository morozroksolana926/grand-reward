package com.Qerxavi7.Nomlut.core.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.Qerxavi7.Nomlut.core.theme.Accent
import kotlin.random.Random

@Composable
fun DecorParticles(
    count: Int,
    animated: Boolean,
    modifier: Modifier = Modifier,
    color: Color = Accent
) {
    val seeds = remember(count) {
        List(count) {
            Triple(Random.nextFloat(), Random.nextFloat(), Random.nextFloat())
        }
    }
    val transition = rememberInfiniteTransition(label = "particles")
    val drift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 7200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particleDrift"
    )
    val phase = if (animated) drift else 0f
    Canvas(modifier = modifier) {
        seeds.forEach { seed ->
            val x = seed.first * size.width
            val travel = (seed.second + phase * (0.6f + seed.third * 0.6f)) % 1f
            val y = size.height * (1f - travel)
            val radius = (1.5f + seed.third * 1.6f) * density
            drawCircle(
                color = color.copy(alpha = 0.10f + seed.third * 0.20f),
                radius = radius,
                center = Offset(x, y)
            )
        }
    }
}
