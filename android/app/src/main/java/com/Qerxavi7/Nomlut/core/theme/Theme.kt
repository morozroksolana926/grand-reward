package com.Qerxavi7.Nomlut.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val GrandRewardColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = TextPrimary,
    primaryContainer = PrimaryBright,
    onPrimaryContainer = TextPrimary,
    secondary = SecondaryRose,
    onSecondary = BackgroundMain,
    tertiary = Accent,
    onTertiary = BackgroundMain,
    background = BackgroundMain,
    onBackground = TextPrimary,
    surface = SurfaceGlass,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = GlassBorder,
    outlineVariant = GlassHighlight,
    error = PrimaryBright,
    onError = TextPrimary,
    scrim = BackgroundDeep
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = GrandRewardColorScheme,
        typography = AppTypography,
        content = content
    )
}
