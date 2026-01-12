package it.unibo.discoverit.data.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.unibo.discoverit.data.database.entities.User
import kotlinx.coroutines.flow.Flow
import it.unibo.discoverit.data.database.entities.PointOfInterest
import it.unibo.discoverit.data.database.entities.Category
import it.unibo.discoverit.data.database.entities.Visit



/**
 * Data Access Object for the [User] entity.
 * Contains methods to interact with the [User] table in the database.
 */
@Dao
interface UserDAO {
    /**
     * Inserts a [User] into the database.
     *
     * @param user the [User] to insert.
     */
    @Insert
    suspend fun insert(user: User): Long

    /**
     * Inserts a list of [User] into the database. If the [User] already exists,
     * it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<User>)

    /**
     * Deletes a [User] from the database.
     *
     * @param user the [User] to delete.
     */
    @Delete
    suspend fun delete(user: User)

    /**
     * Updates a [User] in the database.
     *
     * @param user the [User] to update.
     */
    @Update
    suspend fun update(user: User)

    /**
     * Retrieves all the [users][User] from the database.
     *
     * @return the list of all [users][User]
     */
    @Query("SELECT * FROM users")
    suspend fun getAll(): List<User>

    /**
     * Retrieves the [User] with the given [username] from the database.
     * Reminder that the username is unique, so it should return only one [User].
     *
     * @param username the username of the [User]
     * @return the [User] with the given [username], or null if it doesn't exist.
     */
    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?

    /**
     * Retrieves all the [users][User] that are "friends of" (followed by) the [User] with the
     * given [userId].
     *
     * @param userId the id of the [User] to retrieve the friends of.
     * @return the list of all [users][User] that are "friends of" (followed by) the [User] with
     * the given [userId].
     */
    @Query(
        "SELECT * FROM users WHERE userId IN (SELECT friendId FROM friendships WHERE userId = :userId)"
    )
    fun getFriends(userId: Long): Flow<List<User>>

    /**
     * Get all the [users][User] that are "friends of" (followed by) the [User] with the given
     * [userId] and the number of achievements that they have completed.
     *
     * @param userId the id of the [User] to retrieve the friends of.
     * @return a flow emitting a map of all the [user's][User] friends and the number of
     * achievements that they have completed.
     */
    @Query(
        """
        SELECT users.*, COUNT(user_achievement_progress.achievementId) as completedAchievements
        FROM users
        LEFT JOIN user_achievement_progress ON users.userId = user_achievement_progress.userId AND user_achievement_progress.isCompleted = 1
        WHERE users.userId IN (
            SELECT friendId
            FROM friendships
            WHERE userId = :userId
        )
        GROUP BY 
            users.userId,
            users.username,
            users.hashedPassword,
            users.profilePicPath
    """
    )
    fun getFriendsAndCountCompletedAchievements(userId: Long): Flow<Map<
            @MapColumn(columnName = "userId") User,
            @MapColumn(columnName = "completedAchievements") Long>>

    /**
     * Retrieves the number of achievements that the [User] with the given [userId] have
     * completed.
     *
     * @param userId the id of the [User] to retrieve the achievements of.
     * @return the number of achievements that the [User] with the given [userId] have
     * completed.
     */
    @Query("""
        SELECT COUNT(*)
        FROM user_achievement_progress
        WHERE userId = :userId AND isCompleted = 1
    """)
    fun getCountCompletedAchievements(userId: Long): Flow<Long>

    /**
     * Retrieves the [User] with the given [userId] from the database.
     *
     * @param userId the id of the [User] to retrieve.
     * @return the [User] with the given [userId].
     */
    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserById(userId: Long): User

    /**
     * Retrieves the number of visited [points of interest][PointOfInterest] in a [category][Category]
     * by a given [user][User].
     *
     * @param userId the id of the [User] to filter by.
     * @param categoryId the id of the [Category] to filter by.
     * @return the number of visited [points of interest][PointOfInterest] in the requested
     * [category][Category].
     */
    @Query("""
        SELECT COUNT(*)
        FROM visits
        WHERE userId = :userId AND poiId IN (
            SELECT poiId
            FROM points_of_interest
            WHERE categoryId = :categoryId
        )
    """)
    suspend fun countVisitsForCategory(userId: Long, categoryId: Long): Int

    /**
     * Retrieves the number of [visits][Visit] of a given [user][User].
     *
     * @param userId the id of the [User] to filter by.
     * @return the number of [visits][Visit] of the given [user][User].
     */
    @Query("""
        SELECT COUNT(*)
        FROM visits
        WHERE userId = :userId
    """)
    suspend fun countVisits(userId: Long): Int
}