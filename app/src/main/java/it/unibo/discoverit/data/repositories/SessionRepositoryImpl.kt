package it.unibo.discoverit.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of [SessionRepository] that uses [DataStore] to store session data.
 * @property dataStore the [DataStore] instance used to store session data.
 */
class SessionRepositoryImpl(
    private val dataStore: DataStore<Preferences>
): SessionRepository {
    /**
     * Companion object containing the keys used to store session data in [DataStore].
     */
    companion object {
        private val USER_ID_KEY = longPreferencesKey("user_id")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
    }


    override val isLoggedIn: Flow<Boolean> = dataStore.data.map { it[IS_LOGGED_IN_KEY] ?: false }
    override val loggedUserId: Flow<Long?> = dataStore.data.map { it[USER_ID_KEY] }
    override val loggedUsername: Flow<String?> = dataStore.data.map { it[USERNAME_KEY] }

    override suspend fun saveSession(userId: Long, username: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
            preferences[USERNAME_KEY] = username
            preferences[IS_LOGGED_IN_KEY] = true
        }
    }

    override suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.remove(USER_ID_KEY)
            preferences.remove(USERNAME_KEY)
            preferences[IS_LOGGED_IN_KEY] = false
        }
    }

}