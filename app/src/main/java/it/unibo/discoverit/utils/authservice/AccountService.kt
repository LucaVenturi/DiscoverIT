package it.unibo.discoverit.utils.authservice

import it.unibo.discoverit.data.database.entities.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    suspend fun login(username: String, password: String): User
    suspend fun register(username: String, password: String): User
    suspend fun getCurrentUser(): User?
    suspend fun logout()
    fun isLoggedIn(): Flow<Boolean>
}