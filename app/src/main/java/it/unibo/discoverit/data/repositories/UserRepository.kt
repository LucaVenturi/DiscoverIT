package it.unibo.discoverit.data.repositories

import it.unibo.discoverit.data.database.entities.User
import kotlinx.coroutines.flow.Flow

/**
 * Repository for user-related operations.
 */
interface UserRepository {
    /**
     * Function to login a user.
     * If the login is successful, the user is returned.
     *
     * @param username the username of the user to login.
     * @param plainPassword the password of the user to login.
     * @return the user that has been logged in.
     */
    suspend fun login(username: String, plainPassword: String): User

    /**
     * Function to register a user.
     * If the registration is successful, the user id is returned.
     *
     * @param username the username of the user to register.
     * @param plainPassword the password of the user to register.
     * @return the user id of the user that has been registered.
     */
    suspend fun register(username: String, plainPassword: String): Long

    /**
     * Function to get a [User] by their id.
     *
     * @param userId the id of the user to get.
     * @return the user with the given id.
     */
    suspend fun getUserById(userId: Long): User

    /**
     * Function to update a [User].
     *
     * @param user the user to update.
     */
    suspend fun update(user: User)

    /**
     * Function to delete a [User].
     *
     * @param user the user to delete.
     */
    suspend fun delete(user: User)

    // Friendship management, maybe should move to another repo...
    /**
     * Function to add a friendship between two users.
     *
     * @param userId the id of the user to add the friendship to.
     * @param username the username of the user to add as a friend.
     */
    suspend fun addFriendship(userId: Long, username: String)

    /**
     * Function to remove a friendship between two users.
     *
     * @param userId the id of the user to remove the friendship from.
     */
    suspend fun removeFriendship(userId: Long, friendId: Long)

    /**
     * Function to get all the friends of a user.
     *
     * @param userId the id of the user to get the friends of.
     * @return a flow emitting a list of all the friends of the user.
     */
    fun getFriends(userId: Long): Flow<List<User>>

    /**
     * Function to get all the friends of a user and the number of completed achievements for
     * of each of them.
     *
     * @param userId the id of the user to get the friends of.
     * @return a flow emitting a map of all the friends of the user and the number of completed
     * achievements for each of them.
     */
    fun getFriendsAndCountCompletedAchievements(userId: Long): Flow<Map<User, Long>>

    // Profile picture
    /**
     * Function to update the profile picture of a user.
     *
     * @param userId the id of the user to update the profile picture of.
     * @param path the path to the new profile picture.
     * @return the updated user.
     */
    suspend fun updateProfilePicture(userId: Long, path: String): User

    // Achievements
    /**
     * Function to get the number of completed achievements of a user.
     *
     * @param userId the id of the user to get the completed achievements of.
     * @return a flow emitting the number of completed achievements
     */
    fun getCountCompletedAchievements(userId: Long): Flow<Long>
}