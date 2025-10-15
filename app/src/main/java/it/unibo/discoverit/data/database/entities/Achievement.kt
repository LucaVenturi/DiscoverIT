package it.unibo.discoverit.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

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