package com.Qerxavi7.Nomlut.presentation.app

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Qerxavi7.Nomlut.core.config.GameConfig
import com.Qerxavi7.Nomlut.core.di.ServiceLocator
import com.Qerxavi7.Nomlut.core.navigation.Screen
import com.Qerxavi7.Nomlut.core.theme.BackgroundMain
import com.Qerxavi7.Nomlut.presentation.collection.CollectionScreen
import com.Qerxavi7.Nomlut.presentation.game.GameScreen
import com.Qerxavi7.Nomlut.presentation.gameover.GameOverScreen
import com.Qerxavi7.Nomlut.presentation.menu.MenuScreen
import com.Qerxavi7.Nomlut.presentation.quests.QuestsScreen
import com.Qerxavi7.Nomlut.presentation.splash.SplashScreen

@Composable
fun AppRoot() {
    val activity = LocalActivity.current
    val appViewModel: AppViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()

    BackHandler(enabled = true) {
        if (!appViewModel.back()) activity?.finish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundMain)
    ) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                fadeIn(tween(GameConfig.SCREEN_FADE_IN_MS)) togetherWith
                    fadeOut(tween(GameConfig.SCREEN_FADE_OUT_MS))
            },
            label = "screenTransition"
        ) { target ->
            when (target) {
                Screen.Splash -> SplashScreen(
                    onFinished = appViewModel::onSplashFinished
                )

                Screen.Menu -> MenuScreen(
                    onPlay = appViewModel::openGame,
                    onCollection = appViewModel::openCollection,
                    onQuests = appViewModel::openQuests
                )

                is Screen.Game -> GameScreen(
                    round = target.round,
                    onExit = appViewModel::backToMenu,
                    onFinished = appViewModel::onRoundFinished
                )

                is Screen.GameOver -> GameOverScreen(
                    round = target.round,
                    result = target.result,
                    onPlayAgain = appViewModel::openGame,
                    onMenu = appViewModel::backToMenu
                )

                Screen.Collection -> CollectionScreen(
                    onMenu = appViewModel::backToMenu
                )

                Screen.Quests -> QuestsScreen(
                    onPlay = appViewModel::openGame,
                    onMenu = appViewModel::backToMenu
                )
            }
        }
    }
}
