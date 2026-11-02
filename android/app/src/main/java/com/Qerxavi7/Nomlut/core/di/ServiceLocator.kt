package com.Qerxavi7.Nomlut.core.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.Qerxavi7.Nomlut.data.local.PreferencesStorage
import com.Qerxavi7.Nomlut.data.repository.ProgressRepositoryImpl
import com.Qerxavi7.Nomlut.data.repository.SettingsRepositoryImpl

object ServiceLocator {

    private var factory: AppViewModelFactory? = null

    fun init(context: Context) {
        if (factory != null) return
        val storage = PreferencesStorage(context)
        factory = AppViewModelFactory(
            ProgressRepositoryImpl(storage),
            SettingsRepositoryImpl(storage)
        )
    }

    val viewModelFactory: ViewModelProvider.Factory
        get() = requireNotNull(factory)
}
