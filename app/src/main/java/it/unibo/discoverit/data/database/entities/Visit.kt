package it.unibo.discoverit.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "visits",
    primaryKeys = ["userId", "poiId"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PointOfInterest::class,
            parentColumns = ["poiId"],
            childColumns = ["poiId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Visit(
    val userId: Long,
    val poiId: Long,
    val visitDate: Long,
)
