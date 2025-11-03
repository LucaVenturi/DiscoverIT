package it.unibo.discoverit.data.repositories

import it.unibo.discoverit.ui.screens.settings.ThemeOption
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val theme: Flow<String>
    val notificationsEnabled: Flow<Boolean>
    val biometricLoginEnabled: Flow<Boolean>
    val appVersion: String

    suspend fun setTheme(theme: ThemeOption)
    suspend fun setNotificationsEnabled(enabled: Boolean)
    suspend fun setBiometricLoginEnabled(enabled: Boolean)
}