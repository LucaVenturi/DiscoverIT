package it.unibo.discoverit.data.repositories

import it.unibo.discoverit.data.database.daos.AchievementDAO
import it.unibo.discoverit.data.database.daos.UserDAO
import it.unibo.discoverit.data.database.entities.Achievement
import it.unibo.discoverit.data.database.entities.UserAchievementProgress
import kotlinx.coroutines.flow.Flow


class AchievementRepository(
    private val achievementDAO: AchievementDAO,
    private val userDAO: UserDAO
) {
    fun getAchievementsWithProgress(userId: Long): Flow<Map<Achievement, UserAchievementProgress?>> {
        return achievementDAO.getAchievementsWithProgress(userId)
    }

    suspend fun updateAchievementsProgressForUser(userId: Long, categoryId: Long) {
        val achievements = achievementDAO.getAchievementsByCategory(categoryId)
        achievements.forEach { achievement ->
            val count = if (achievement.targetCategory != null) {
                userDAO.countVisitsForCategory(userId, achievement.targetCategory)
            } else {
                userDAO.countVisits(userId)
            }

            val isCompleted = count >= achievement.targetCount

            achievementDAO.upsertUserAchievementProgress(
                UserAchievementProgress(
                    userId = userId,
                    achievementId = achievement.achievementId,
                    progress = count,
                    isCompleted = isCompleted,
                    completionDate = if (isCompleted) System.currentTimeMillis() else null
                )
            )
        }
    }
}