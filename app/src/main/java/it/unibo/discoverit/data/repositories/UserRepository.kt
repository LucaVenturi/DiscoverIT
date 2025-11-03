package it.unibo.discoverit.data.repositories

import it.unibo.discoverit.data.database.entities.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(username: String, plainPassword: String): User
    suspend fun register(username: String, plainPassword: String): Long
    suspend fun getUserById(userId: Long): User
    suspend fun update(user: User)
    suspend fun delete(user: User)

    // Friendship management
    suspend fun addFriendship(userId: Long, username: String)
    suspend fun removeFriendship(userId: Long, friendId: Long)
    fun getFriends(userId: Long): Flow<List<User>>
    fun getFriendsAndCountCompletedAchievements(userId: Long): Flow<Map<User, Long>>

    // Profile picture
    suspend fun updateProfilePicture(userId: Long, path: String): User

    // Achievements
    fun getCountCompletedAchievements(userId: Long): Flow<Long>
}