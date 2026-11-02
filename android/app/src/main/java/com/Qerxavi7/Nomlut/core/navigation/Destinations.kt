package com.Qerxavi7.Nomlut.core.navigation

import com.Qerxavi7.Nomlut.domain.model.GameResult

sealed interface Screen {

    data object Splash : Screen

    data object Menu : Screen

    data class Game(val round: Int) : Screen

    data class GameOver(val round: Int, val result: GameResult) : Screen

    data object Collection : Screen

    data object Quests : Screen
}
