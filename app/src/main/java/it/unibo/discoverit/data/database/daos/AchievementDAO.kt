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

/**
 * Data Access Object for the [Achievement] entity.
 */
@Dao
interface AchievementDAO {
    /**
     * Inserts a list of [Achievement] into the database. If the [Achievement] already exists,
     * it will be replaced.
     * @param achievements the list of [Achievement] to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(achievements: List<Achievement>)

    /**
     * Returns a flow containing the list of all [Achievement] from the database.
     */
    @Query("SELECT * FROM achievements")
    fun getAll(): Flow<List<Achievement>>

    /**
     * Returns the [Achievement] with the given [id] from the database.
     * @param id the id of the [Achievement] to retrieve.
     */
    @Query("SELECT * FROM achievements WHERE achievementId = :id")
    suspend fun getById(id: Long): Achievement?

    /**
     * Returns a flow containing the list of [UserAchievementProgress] for the given [userId]
     * from the database.
     * @param userId the id of the user to retrieve the [UserAchievementProgress] for.
     */
    @Query("""
        SELECT uap.* 
        FROM user_achievement_progress AS uap
        WHERE uap.userId = :userId
    """)
    fun getUserAchievementsProgress(userId: Long): Flow<List<UserAchievementProgress>>

    /**
     * Inserts a new [Achievement] into the database.
     * @param achievement the [Achievement] to insert.
     */
    @Insert
    suspend fun insert(achievement: Achievement): Long

    /**
     * Updates an existing [Achievement] in the database.
     * If the [Achievement] does not exist, it won't be inserted.
     * @param achievement the [Achievement] to update.
     */
    @Update
    suspend fun update(achievement: Achievement)

    /**
     * Returns a list of [Achievement] from the database that are associated with the given [categoryId].
     * @param categoryId the id of the category to retrieve the [Achievement] for.
     */
    @Query("""
        SELECT * FROM achievements 
        WHERE targetCategory = :categoryId OR targetCategory IS NULL
    """)
    suspend fun getAchievementsByCategory(categoryId: Long): List<Achievement>

    /**
     * Returns the [UserAchievementProgress] for the given [userId] and [achievementId] from the database.
     * @param userId the id of the user to retrieve the [UserAchievementProgress] for.
     * @param achievementId the id of the [Achievement] to retrieve the [UserAchievementProgress] for.
     * @return the [UserAchievementProgress] for the given [userId] and [achievementId] from the database.
     */
    @Query("""
        SELECT uap.* 
        FROM user_achievement_progress AS uap
        WHERE uap.userId = :userId AND uap.achievementId = :achievementId
    """)
    suspend fun getUserAchievementProgress(userId: Long, achievementId: Long): UserAchievementProgress?

    /**
     * Inserts a new [UserAchievementProgress] into the database.
     * If the [UserAchievementProgress] already exists, it will be replaced.
     * @param userAchievementProgress the [UserAchievementProgress] to insert.
     */
    @Upsert
    suspend fun upsertUserAchievementProgress(userAchievementProgress: UserAchievementProgress)

    /**
     * Retrieves all the [Achievement] along with their [UserAchievementProgress] for the given
     * [userId] from the database.
     * @param userId the id of the user to retrieve the [Achievement] for.
     * @return a [Flow] emitting a [Map] of [Achievement] associated with their [UserAchievementProgress].
     */
    @Query("""
        SELECT a.*, uap.*
        FROM achievements AS a
        LEFT JOIN user_achievement_progress AS uap
        ON a.achievementId = uap.achievementId
            AND uap.userId = :userId
    """)
    fun getAchievementsWithProgress(userId: Long): Flow<Map<Achievement, UserAchievementProgress>>
}