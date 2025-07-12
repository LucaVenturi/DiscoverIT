package it.unibo.discoverit.data.database

import it.unibo.discoverit.data.database.entities.Achievement
import it.unibo.discoverit.data.database.entities.Category
import it.unibo.discoverit.data.database.entities.Friendship
import it.unibo.discoverit.data.database.entities.PointOfInterest
import it.unibo.discoverit.data.database.entities.User
import it.unibo.discoverit.data.database.entities.UserAchievementProgress
import it.unibo.discoverit.data.database.entities.Visit
import kotlinx.serialization.Serializable

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