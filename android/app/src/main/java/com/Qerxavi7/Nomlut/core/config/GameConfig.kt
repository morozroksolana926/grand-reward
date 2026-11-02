package com.Qerxavi7.Nomlut.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L

    const val COLS = 5
    const val ROWS = 6
    const val MIN_GROUP = 2
    const val MOVES_PER_STAGE = 22
    const val BASE_TARGET = 1200
    const val TARGET_STEP = 450
    const val TOTAL_STAGES = 12
    const val RELIC_COUNT = 5

    const val BOARD_BUILD_MS = 250L
    const val COLLAPSE_HIGHLIGHT_MS = 180L
    const val COLLAPSE_FADE_MS = 200L
    const val REFILL_MS = 180L
    const val INVALID_SHAKE_MS = 160L
    const val RESHUFFLE_MS = 400L
    const val CHAIN_WINDOW_MS = 2000L
    const val HINT_PULSE_MS = 1200L

    const val SCORE_COUNTUP_MS = 900
    const val SCREEN_FADE_IN_MS = 420
    const val SCREEN_FADE_OUT_MS = 320

    const val CLEAR_BONUS_PER_MOVE = 60
    const val STAGE_CLEAR_RP = 250
    const val RP_PER_POINT = 10
    const val CHAIN_STEP = 0.15f
    const val CHAIN_CAP = 2.0f
}
