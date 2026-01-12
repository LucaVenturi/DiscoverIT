package it.unibo.discoverit.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import kotlinx.serialization.Serializable

/**
 * Represents a visit to a [PointOfInterest] by a [User].
 * A visit is a relation between a [User] and a [PointOfInterest].
 *
 * This entity is stored in the `visits` table.
 *
 * @property userId the id of the [User] who visited the [PointOfInterest].
 * @property poiId the id of the [PointOfInterest] visited by the [User].
 * @property visitDate the date of the visit.
 */
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
    ],
    indices = [androidx.room.Index("userId"), androidx.room.Index("poiId")]
)
data class Visit(
    val userId: Long,
    val poiId: Long,
    val visitDate: Long,
)
