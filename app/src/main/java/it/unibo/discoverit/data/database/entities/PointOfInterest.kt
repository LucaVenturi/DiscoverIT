package it.unibo.discoverit.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Represents a point of interest in the application.
 *
 * A point of interest is a location that the user can visit.
 * It is associated with a [Category] and have an optional image.
 *
 * This entity is stored in the `points_of_interest` table.
 *
 * @property poiId unique identifier of the point of interest.
 * @property name display name of the point of interest.
 * @property description textual description of the point of interest.
 * @property latitude latitude of the point of interest.
 * @property longitude longitude of the point of interest.
 * @property imagePath path to the image of the point of interest. As of right now images are stored in the assets folder.
 * @property address [Address] of the point of interest.
 */
@Serializable
@Entity(
    tableName = "points_of_interest",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [androidx.room.Index("categoryId")]
)
data class PointOfInterest(
    @PrimaryKey(autoGenerate = true) val poiId: Long = 0,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val imagePath: String?,
    @Embedded val address: Address,
    val categoryId: Long
)

/**
 * Represents an address. Is used as an @Embedded in the [PointOfInterest] entity.
 * Might consider adding POSTAL CODE (CAP in italian) in the future.
 *
 * @property street name of the street.
 * @property civicNumber number of the civic.
 * @property province name of the province.
 *
 */
@Serializable
data class Address(
    val street: String,       // Via/piazza
    val civicNumber: String?, // Civico (opzionale)
    val province: String,
)