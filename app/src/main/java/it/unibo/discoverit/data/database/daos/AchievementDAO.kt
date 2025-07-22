package it.unibo.discoverit.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
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

    @Query("""
        SELECT * FROM achievements 
        WHERE targetCategory = :categoryId OR targetCategory IS NULL
    """)
    suspend fun getAchievementsByCategory(categoryId: Long): List<Achievement>

    @Query("""
        SELECT uap.* 
        FROM user_achievement_progress AS uap
        WHERE uap.userId = :userId AND uap.achievementId = :achievementId
    """)
    suspend fun getUserAchievementProgress(userId: Long, achievementId: Long): UserAchievementProgress?

    @Upsert
    suspend fun upsertUserAchievementProgress(userAchievementProgress: UserAchievementProgress)

    @Query("""
        SELECT a.*, uap.*
        FROM achievements AS a
        LEFT JOIN user_achievement_progress AS uap
        ON a.achievementId = uap.achievementId
            AND uap.userId = :userId
    """)
    fun getAchievementsWithProgress(userId: Long): Flow<Map<Achievement, UserAchievementProgress?>>
}