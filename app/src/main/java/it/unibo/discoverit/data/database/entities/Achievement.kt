package it.unibo.discoverit.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Represents an achievement that can be unlocked by the user.
 *
 * An achievement defines a target objective that can optionally be associated
 * with a specific [Category]. Progress towards the achievement is tracked
 * separately.
 *
 * This entity is stored in the `achievements` table.
 *
 * @property achievementId unique identifier of the achievement
 * @property name display name of the achievement
 * @property description textual description of the achievement objective
 * @property targetCount required value to unlock the achievement
 * @property targetCategory optional identifier of the target [Category];
 * null if the achievement is not category-specific
 */
@Serializable
@Entity(
    tableName = "achievements",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["categoryId"],
            childColumns = ["targetCategory"],
        )
    ],
    indices = [androidx.room.Index("targetCategory")]
)
data class Achievement(
    @PrimaryKey(autoGenerate = true) val achievementId: Long = 0,
    val name: String,
    val description: String,
    val targetCount: Int,
    val targetCategory: Long?,
)