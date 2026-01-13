package it.unibo.discoverit.data.repositories

import it.unibo.discoverit.data.database.daos.AchievementDAO
import it.unibo.discoverit.data.database.daos.UserDAO
import it.unibo.discoverit.data.database.entities.Achievement
import it.unibo.discoverit.data.database.entities.UserAchievementProgress
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of [AchievementRepository] that uses DAOs to access achievement data.
 *
 * @property achievementDAO DAO for achievement-related database operations.
 * @property userDAO DAO for user-related database operations.
 */
class AchievementRepositoryImpl(
    private val achievementDAO: AchievementDAO,
    private val userDAO: UserDAO
): AchievementRepository {

    override fun getAchievementsWithProgress(userId: Long): Flow<Map<Achievement, UserAchievementProgress?>> {
        return achievementDAO.getAchievementsWithProgress(userId)
    }

    override suspend fun updateAchievementsProgressForUser(userId: Long, categoryId: Long) {
        // Recupera tutti gli achievement associati alla categoria
        val achievements = achievementDAO.getAchievementsByCategory(categoryId)

        // Calcola le visite totali dell'utente
        val userTotalVisits = userDAO.countVisits(userId)

        achievements.forEach { achievement ->
            // Se è un achievement specifico, conta le visite dell'utente per la
            // categoria target.
            val count = if (achievement.targetCategory != null) {
                userDAO.countVisitsForCategory(userId, achievement.targetCategory)
            } else {
                userTotalVisits
            }

            // Verifica se l'achievement è stato completato
            val isCompleted = count >= achievement.targetCount

            // Recupera il progresso esistente per preservare la data di completamento originale
            val existingProgress = achievementDAO.getUserAchievementProgress(
                userId = userId,
                achievementId = achievement.achievementId
            )

            // Determina la data di completamento
            val completionDate = when {
                !isCompleted -> null  // Non ancora completato
                existingProgress?.isCompleted == true -> existingProgress.completionDate  // Già completato, mantieni data originale
                else -> System.currentTimeMillis()  // Appena completato, salva data attuale
            }

            // Aggiorna o inserisce il progresso dell'achievement
            achievementDAO.upsertUserAchievementProgress(
                UserAchievementProgress(
                    userId = userId,
                    achievementId = achievement.achievementId,
                    progress = count,
                    isCompleted = isCompleted,
                    completionDate = completionDate
                )
            )
        }
    }
}