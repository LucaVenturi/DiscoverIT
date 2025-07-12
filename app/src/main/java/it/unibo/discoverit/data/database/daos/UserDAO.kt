package it.unibo.discoverit.data.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.unibo.discoverit.data.database.entities.Achievement
import it.unibo.discoverit.data.database.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {
    @Insert
    suspend fun insert(user: User): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<User>)

    @Delete
    suspend fun delete(user: User): Unit

    @Update
    suspend fun update(user: User): Unit

    @Query("SELECT * FROM users")
    suspend fun getAll(): List<User>

//    @Query(
//        "SELECT * FROM users WHERE username = :username AND password = :password"
//    )
//    suspend fun login(username: String, password: String): User?

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?

    @Query(
        "SELECT * FROM users WHERE userId IN (SELECT friendId FROM friendships WHERE userId = :userId)"
    )
    fun getFriends(userId: Long): Flow<List<User>>

    @Query(
        "SELECT * " +
                "FROM achievements " +
                "WHERE achievementId IN " +
                "(SELECT achievementId FROM user_achievement_progress WHERE userId = :userId AND isCompleted = 1)"
    )
    fun getCompletedAchievements(userId: Long): Flow<List<Achievement>>
}