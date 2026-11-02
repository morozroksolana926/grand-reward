package com.Qerxavi7.Nomlut.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Qerxavi7.Nomlut.domain.repository.ProgressRepository
import com.Qerxavi7.Nomlut.domain.repository.SettingsRepository
import com.Qerxavi7.Nomlut.presentation.app.AppViewModel
import com.Qerxavi7.Nomlut.presentation.collection.CollectionViewModel
import com.Qerxavi7.Nomlut.presentation.game.GameViewModel
import com.Qerxavi7.Nomlut.presentation.gameover.GameOverViewModel
import com.Qerxavi7.Nomlut.presentation.menu.MenuViewModel
import com.Qerxavi7.Nomlut.presentation.quests.QuestsViewModel
import com.Qerxavi7.Nomlut.presentation.splash.SplashViewModel

class AppViewModelFactory(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) ->
                AppViewModel()

            modelClass.isAssignableFrom(SplashViewModel::class.java) ->
                SplashViewModel()

            modelClass.isAssignableFrom(MenuViewModel::class.java) ->
                MenuViewModel(progressRepository, settingsRepository)

            modelClass.isAssignableFrom(GameViewModel::class.java) ->
                GameViewModel(progressRepository, settingsRepository)

            modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
                GameOverViewModel(progressRepository)

            modelClass.isAssignableFrom(CollectionViewModel::class.java) ->
                CollectionViewModel(progressRepository)

            modelClass.isAssignableFrom(QuestsViewModel::class.java) ->
                QuestsViewModel(progressRepository)

            else -> throw IllegalArgumentException(modelClass.name)
        }
        return created as T
    }
}
