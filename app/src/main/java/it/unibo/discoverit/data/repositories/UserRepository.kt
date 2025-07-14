package it.unibo.discoverit.data.repositories

import android.accounts.AuthenticatorException
import it.unibo.discoverit.data.database.daos.UserDAO
import it.unibo.discoverit.data.database.entities.Achievement
import it.unibo.discoverit.data.database.entities.User
import it.unibo.discoverit.utils.hasher.PasswordHasher
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userDao: UserDAO,
    private val passwordHasher: PasswordHasher
) {
    suspend fun login(username: String, plainPassword: String): User {
        val user = userDao.getUserByUsername(username)
            ?: throw AuthenticatorException("User not found")

        if (!passwordHasher.verifyPassword(plainPassword, user.hashedPassword)) {
            throw AuthenticatorException("Invalid credentials")
        }

        return user
    }

    suspend fun register(username: String, plainPassword: String, profilePicPath: String? = null): Long {
        if (userDao.getUserByUsername(username) != null) {
            throw AuthenticatorException("Username already exists")
        }

        val hashedPassword = passwordHasher.hashPassword(plainPassword)
        val user = User(
            username = username,
            hashedPassword = hashedPassword,
            profilePicPath = profilePicPath
        )

        return userDao.insert(user)
    }

//    fun getUserByUsername(username: String): Flow<User?> =
//        userDao.getUserByUsername(username)

    fun getFriends(userId: Long): Flow<List<User>> =
        userDao.getFriends(userId)

    fun getFriendsAndCountCompletedAchievements(userId: Long): Flow<Map<User, Long>> =
        userDao.getFriendsAndCountCompletedAchievements(userId)

    private suspend fun insert(user: User): Long =
        userDao.insert(user)

    suspend fun update(user: User) =
        userDao.update(user)

    suspend fun delete(user: User) =
        userDao.delete(user)

    fun getCompletedAchievements(userId: Long): Flow<List<Achievement>> =
        userDao.getCompletedAchievements(userId)
}
