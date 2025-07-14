package it.unibo.discoverit.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.unibo.discoverit.data.database.entities.Friendship
import it.unibo.discoverit.data.database.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendshipDAO {
    @Insert
    suspend fun insert(friendship: Friendship)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(friendships: List<Friendship>)

    @Query("DELETE FROM friendships WHERE userId = :userId AND friendId = :friendId")
    suspend fun delete(userId: Long, friendId: Long)

    @Query("""
        SELECT COUNT(*) FROM friendships 
        WHERE userId = :userId AND friendId = :friendId
    """)
    suspend fun isFriend(userId: Long, friendId: Long): Boolean

}