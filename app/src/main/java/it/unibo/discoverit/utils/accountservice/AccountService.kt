package it.unibo.discoverit.utils.accountservice

import it.unibo.discoverit.data.database.entities.User
import kotlinx.coroutines.flow.Flow

/**
 * Interface for a service that handles user accounts.
 * Provides methods for logging in, registering, logging out and methods for the session.
 */
interface AccountService {
    /**
     * Logs in a user with the given username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The logged-in user.
     */
    suspend fun login(username: String, password: String): User

    /**
     * Registers a new user with the given username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     */
    suspend fun register(username: String, password: String): User

    /**
     * Gets the currently logged-in user if the session exists, otherwise returns null.
     *
     * @return The currently logged-in user, or null if no user is logged in.
     */
    suspend fun getCurrentUser(): User?

    /**
     * Logs out the current user by clearing the session.
     */
    suspend fun logout()

    /**
     * Checks if the user is logged in.
     *
     * @return A flow that emits true if the user is logged in, false otherwise.
     */
    fun isLoggedIn(): Flow<Boolean>
}