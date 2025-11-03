package it.unibo.discoverit.data.repositories

import it.unibo.discoverit.data.database.entities.Achievement
import it.unibo.discoverit.data.database.entities.UserAchievementProgress
import kotlinx.coroutines.flow.Flow

interface AchievementRepository {
    fun getAchievementsWithProgress(userId: Long): Flow<Map<Achievement, UserAchievementProgress?>>
    suspend fun updateAchievementsProgressForUser(userId: Long, categoryId: Long)
}