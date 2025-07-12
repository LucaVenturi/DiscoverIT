package it.unibo.discoverit.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.unibo.discoverit.data.database.entities.Achievement
import it.unibo.discoverit.data.database.entities.UserAchievementProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(achievements: List<Achievement>)

    @Query("SELECT * FROM achievements")
    fun getAll(): Flow<List<Achievement>>

    @Query("SELECT * FROM achievements WHERE achievementId = :id")
    suspend fun getById(id: Long): Achievement?

    @Query("""
        SELECT uap.* 
        FROM user_achievement_progress AS uap
        WHERE uap.userId = :userId
    """)
    fun getUserAchievementsProgress(userId: Long): Flow<List<UserAchievementProgress>>

    @Insert
    suspend fun insert(achievement: Achievement): Long

    @Update
    suspend fun update(achievement: Achievement)
}