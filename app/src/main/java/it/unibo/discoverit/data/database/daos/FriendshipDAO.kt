package it.unibo.discoverit.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.unibo.discoverit.data.database.entities.Friendship
import it.unibo.discoverit.data.database.entities.User

/**
 * Data Access Object for the [Friendship] entity.
 * Contains methods to interact with the [Friendship] table in the database, and
 * methods to manage the [Friendship] between two [users][User].
 */
@Dao
interface FriendshipDAO {
    /**
     * Inserts a [Friendship] into the database.
     *
     * @param friendship the [Friendship] to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(friendship: Friendship)

    /**
     * Inserts a list of [Friendship] into the database. If the [Friendship] already exists,
     * it will be replaced.
     * @param friendships the list of [Friendship] to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(friendships: List<Friendship>)

    /**
     * Deletes a [Friendship] from the database given the ids of the [users][User] involved.
     * @param userId the id of the [User] that is following the other [User].
     * @param friendId the id of the [User] that is followed by the other [User].
     */
    @Query("DELETE FROM friendships WHERE userId = :userId AND friendId = :friendId")
    suspend fun delete(userId: Long, friendId: Long)

    /**
     * Retrieves if the [User] with the given [userId] is following the [User] with the given
     * [friendId].
     * @param userId the id of the [User] that is following the other [User].
     * @param friendId the id of the [User] that is followed by the other [User].
     * @return true if the [User] with the given [userId] is following the [User] with the given
     */
    @Query("""
        SELECT EXISTS (
            SELECT 1 FROM friendships
            WHERE userId = :userId AND friendId = :friendId
        )
    """)
    suspend fun isFriend(userId: Long, friendId: Long): Boolean

}