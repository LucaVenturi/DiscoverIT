package it.unibo.discoverit.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import it.unibo.discoverit.ui.screens.settings.ThemeOption
import kotlinx.coroutines.flow.map

class SettingsRepository(
    private val dataStore: DataStore<androidx.datastore.preferences.core.Preferences>
) {
    companion object {
        private val THEME_KEY = stringPreferencesKey("theme")
        private val NOTIFICATIONS_KEY = booleanPreferencesKey("notifications")
    }

    val theme = dataStore.data.map { it[THEME_KEY] ?: "" }
    val notificationsEnabled = dataStore.data.map { it[NOTIFICATIONS_KEY] ?: true }
    val appVersion = "1.0.0"

    suspend fun setTheme(theme: ThemeOption) { dataStore.edit { it[THEME_KEY] = theme.name } }
    suspend fun setNotificationsEnabled(enabled: Boolean) { dataStore.edit { it[NOTIFICATIONS_KEY] = enabled } }
}
