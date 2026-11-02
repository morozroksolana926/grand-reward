package com.Qerxavi7.Nomlut.core.ui

import java.util.Locale

fun formatPoints(value: Int): String = String.format(Locale.US, "%,d", value)

fun formatMultiplier(value: Float): String = String.format(Locale.US, "%.1f", value)
