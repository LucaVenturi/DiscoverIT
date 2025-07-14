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
    fun getCompletedAchievements(userId: Long): Flow<List<Achievement>> {
        return userDAO.getCompletedAchievements(userId)
    }

    fun getAchievementProgress(userId: Long): Flow<List<UserAchievementProgress>> {
        return achievementDAO.getUserAchievementsProgress(userId)
    }

    fun getToDoAchievements(userId: Long): Flow<List<Achievement>> {
        return userDAO.getToDoAchievements(userId)
    }
}