package it.unibo.discoverit.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import kotlinx.serialization.Serializable

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
    ]
)
data class UserAchievementProgress(
    val userId: Long,
    val achievementId: Long,
    val progress: Int = 0,
    val isCompleted: Boolean = false,
    val completionDate: Long? = null,
)
