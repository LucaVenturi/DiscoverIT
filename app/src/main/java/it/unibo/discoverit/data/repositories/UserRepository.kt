package it.unibo.discoverit.data.repositories

import android.accounts.AuthenticatorException
import it.unibo.discoverit.data.database.daos.FriendshipDAO
import it.unibo.discoverit.data.database.daos.UserDAO
import it.unibo.discoverit.data.database.entities.Friendship
import it.unibo.discoverit.data.database.entities.User
import it.unibo.discoverit.utils.hasher.PasswordHasher
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userDao: UserDAO,
    private val friendShipDao: FriendshipDAO,
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

    suspend fun register(username: String, plainPassword: String): Long {
        if (userDao.getUserByUsername(username) != null) {
            throw AuthenticatorException("Username already exists")
        }

        val hashedPassword = passwordHasher.hashPassword(plainPassword)
        val user = User(
            username = username,
            hashedPassword = hashedPassword,
            profilePicPath = null,
            profilePicLastModified = null
        )
        return userDao.insert(user)
    }

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

    suspend fun addFriendship(userId: Long, username: String) {
        val friendId = (userDao.getUserByUsername(username) ?: throw Exception("User not found")).userId

        when {
            userId == friendId -> throw Exception("You cannot add yourself as a friend")
            friendShipDao.isFriend(userId, friendId) -> throw Exception("Friendship already exists")
            else -> friendShipDao.insert(
                Friendship(
                    userId = userId,
                    friendId = friendId,
                    friendshipDate = System.currentTimeMillis()
                )
            )
        }
    }

    suspend fun removeFriendship(userId: Long, friendId: Long) {
        friendShipDao.delete(userId, friendId)
    }

    suspend fun getUserById(userId: Long): User {
        return userDao.getUserById(userId)
    }

    suspend fun updateProfilePicture(userId: Long, path: String): User {
        val user = userDao.getUserById(userId)
        val updatedUser = user.copy(profilePicPath = path, profilePicLastModified = System.currentTimeMillis())

        userDao.update(updatedUser)
        return updatedUser
    }
}
