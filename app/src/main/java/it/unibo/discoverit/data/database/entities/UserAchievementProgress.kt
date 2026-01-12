package it.unibo.discoverit.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import kotlinx.serialization.Serializable

/**
 * Represents the progress of an [Achievement] for a [User].
 * It's the relation between a [User] and an [Achievement].
 *
 * This entity is stored in the `user_achievement_progress` table.
 *
 * @property userId the id of the [User] whom we want to track the progress.
 * @property achievementId the id of the [Achievement] we want to track..
 * @property progress the current progress of the [User] towards the [Achievement].
 * @property isCompleted whether the [User] has completed the [Achievement].
 * @property completionDate the date when the [User] completed the [Achievement].
 */
@Serializable
@Entity(
    tableName = "user_achievement_progress",
    primaryKeys = ["userId", "achievementId"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Achievement::class,
            parentColumns = ["achievementId"],
            childColumns = ["achievementId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [androidx.room.Index("userId"), androidx.room.Index("achievementId")]
)
data class UserAchievementProgress(
    val userId: Long,
    val achievementId: Long,
    val progress: Int = 0,
    val isCompleted: Boolean = false,
    val completionDate: Long? = null,
)
