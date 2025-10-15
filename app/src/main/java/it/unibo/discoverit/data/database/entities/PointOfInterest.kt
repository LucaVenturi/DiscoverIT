package it.unibo.discoverit.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

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

@Serializable
data class Address(
    val street: String,       // Via/piazza
    val civicNumber: String?, // Civico (opzionale)
    val province: String,
)