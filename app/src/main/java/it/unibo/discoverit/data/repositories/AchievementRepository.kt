package it.unibo.discoverit.data.repositories

import it.unibo.discoverit.data.database.entities.Achievement
import it.unibo.discoverit.data.database.entities.UserAchievementProgress
import kotlinx.coroutines.flow.Flow
import it.unibo.discoverit.data.database.entities.User


/**
 * Repository for achievement-related operations.
 */
interface AchievementRepository {
    /**
     * Retrieves all the [achievements][Achievement] with their [progress][UserAchievementProgress]
     * for the given [userId].
     *
     * @param userId the id of the [User] whose [achievements][Achievement] are to be retrieved.
     * @return a flow emitting a map of all the [achievements][Achievement] with their
     * [progress][UserAchievementProgress].
     */
    fun getAchievementsWithProgress(userId: Long): Flow<Map<Achievement, UserAchievementProgress?>>

    /**
     * Updates the [achievement progress][UserAchievementProgress] for a user based on their
     * visits in a specific category.
     *
     * This method retrieves all achievements related to the given category, counts the user's
     * visits, and updates or creates the progress records accordingly. If the target count is
     * reached, the achievement is marked as completed.
     *
     * @param userId the id of the [User] whose achievement progress is to be updated.
     * @param categoryId the id of the category for which to update achievement progress.
     */
    suspend fun updateAchievementsProgressForUser(userId: Long, categoryId: Long)
}