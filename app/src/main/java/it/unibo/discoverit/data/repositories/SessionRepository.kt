package it.unibo.discoverit.data.repositories

import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    val isLoggedIn: Flow<Boolean>
    val loggedUserId: Flow<Long?>
    val loggedUsername: Flow<String?>

    suspend fun saveSession(userId: Long, username: String)
    suspend fun clearSession()
}