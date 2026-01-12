package it.unibo.discoverit.data.database

import it.unibo.discoverit.data.database.entities.Achievement
import it.unibo.discoverit.data.database.entities.Category
import it.unibo.discoverit.data.database.entities.Friendship
import it.unibo.discoverit.data.database.entities.PointOfInterest
import it.unibo.discoverit.data.database.entities.User
import it.unibo.discoverit.data.database.entities.UserAchievementProgress
import it.unibo.discoverit.data.database.entities.Visit
import kotlinx.serialization.Serializable

/**
 * Represents the data stored in the database.
 * Used to populate the database with initial data.
 *
 * @property categories the list of [categories][Category] in the database.
 * @property pointsOfInterest the list of [points of interest][PointOfInterest] in the database.
 * @property achievements the list of [achievements][Achievement] in the database.
 * @property users the list of [users][User] in the database.
 * @property visits the list of [visits][Visit] in the database.
 * @property friendships the list of [friendships][Friendship] in the database.
 * @property userAchievementProgress the list of
 * [user achievement progress][UserAchievementProgress] in the database.
 *
 */
@Serializable
data class DatabaseData(
    val categories: List<Category>,
    val pointsOfInterest: List<PointOfInterest>,
    val achievements: List<Achievement>,
    val users: List<User>,
    val visits: List<Visit>,
    val friendships: List<Friendship>,
    val userAchievementProgress: List<UserAchievementProgress>
)