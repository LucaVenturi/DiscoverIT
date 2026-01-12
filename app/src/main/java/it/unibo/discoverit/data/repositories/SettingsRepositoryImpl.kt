package it.unibo.discoverit.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import it.unibo.discoverit.ui.screens.settings.ThemeOption
import kotlinx.coroutines.flow.map

/**
 * Implementation of [SettingsRepository] that uses [DataStore] to store settings data.
 *
 * @property dataStore the [DataStore] instance used to store settings data.
 */
class SettingsRepositoryImpl(
    private val dataStore: DataStore<androidx.datastore.preferences.core.Preferences>
): SettingsRepository {

    /**
     * Companion object containing the keys used to store settings data in [DataStore].
     */
    companion object {
        private val THEME_KEY = stringPreferencesKey("theme")
        private val NOTIFICATIONS_KEY = booleanPreferencesKey("notifications")
        private val BIOMETRIC_LOGIN_KEY = booleanPreferencesKey("biometric_login")
    }

    override val theme = dataStore.data.map { it[THEME_KEY] ?: ThemeOption.SYSTEM.name }
    override val notificationsEnabled = dataStore.data.map { it[NOTIFICATIONS_KEY] ?: true }
    override val biometricLoginEnabled = dataStore.data.map { it[BIOMETRIC_LOGIN_KEY] ?: false }
    override val appVersion = "1.0.0"

    override suspend fun setTheme(theme: ThemeOption) {
        dataStore.edit { it[THEME_KEY] = theme.name }
    }
    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { it[NOTIFICATIONS_KEY] = enabled }
    }
    override suspend fun setBiometricLoginEnabled(enabled: Boolean) {
        dataStore.edit { it[BIOMETRIC_LOGIN_KEY] = enabled }
    }
}