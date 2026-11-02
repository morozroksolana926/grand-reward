package com.Qerxavi7.Nomlut.presentation.app

import androidx.lifecycle.ViewModel
import com.Qerxavi7.Nomlut.core.navigation.Screen
import com.Qerxavi7.Nomlut.domain.model.GameResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val backStack = ArrayDeque<Screen>()
    private var roundCounter = 0

    fun onSplashFinished() {
        backStack.clear()
        _screen.value = Screen.Menu
    }

    fun openGame() {
        roundCounter += 1
        val current = _screen.value
        if (current !is Screen.GameOver) push(Screen.Game(roundCounter)) else _screen.value = Screen.Game(roundCounter)
    }

    fun openCollection() {
        push(Screen.Collection)
    }

    fun openQuests() {
        push(Screen.Quests)
    }

    fun onRoundFinished(result: GameResult) {
        _screen.value = Screen.GameOver(roundCounter, result)
    }

    fun backToMenu() {
        backStack.clear()
        _screen.value = Screen.Menu
    }

    fun back(): Boolean {
        val previous = backStack.removeLastOrNull() ?: return false
        _screen.value = previous
        return true
    }

    private fun push(target: Screen) {
        val current = _screen.value
        if (current != Screen.Splash) backStack.addLast(current)
        _screen.value = target
    }
}
