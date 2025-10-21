package it.unibo.discoverit.utils.accountservice

import it.unibo.discoverit.data.database.entities.User
import it.unibo.discoverit.data.repositories.SessionRepository
import it.unibo.discoverit.data.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class AccountServiceImpl(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) : AccountService {

    override suspend fun login(username: String, password: String): User {
        val user = userRepository.login(username, password)
        sessionRepository.saveSession(user.userId, user.username)
        return user
    }

    override suspend fun register(username: String, password: String): User {
        val userId = userRepository.register(username, password)
        val user = userRepository.getUserById(userId)
        sessionRepository.saveSession(user.userId, user.username)
        return user
    }

    override suspend fun getCurrentUser(): User? {
        val userId = sessionRepository.loggedUserId.first()
        return userId?.let { userRepository.getUserById(it) }
    }

    override suspend fun logout() {
        sessionRepository.clearSession()
    }

    override fun isLoggedIn(): Flow<Boolean> = sessionRepository.isLoggedIn
}