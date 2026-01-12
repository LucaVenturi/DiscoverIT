package it.unibo.discoverit.data.repositories

import kotlinx.coroutines.flow.Flow

/**
 * Repository for session-related operations.
 *
 * @property isLoggedIn a flow emitting a boolean indicating whether a user is logged in.
 * @property loggedUserId a flow emitting the id of the logged user.
 * @property loggedUsername a flow emitting the username of the logged user.
 */
interface SessionRepository {
    val isLoggedIn: Flow<Boolean>
    val loggedUserId: Flow<Long?>
    val loggedUsername: Flow<String?>

    /**
     * Saves a session for the given [userId] and [username].
     */
    suspend fun saveSession(userId: Long, username: String)

    /**
     * Clears the current session.
     */
    suspend fun clearSession()
}