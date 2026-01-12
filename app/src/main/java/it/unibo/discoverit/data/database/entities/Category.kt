package it.unibo.discoverit.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Represents a category of points of interest.
 *
 * This entity is stored in the `categories` table.
 *
 * @property categoryId unique identifier of the [Category].
 * @property name display name of the [Category].
 * @property iconName name of the icon to use for the [Category].
 */
@Serializable
@Entity(
    tableName = "categories",
)
data class Category(
    @PrimaryKey(autoGenerate = true) val categoryId: Long = 0,
    val name: String,
    val iconName: String? = null
)

/**
 * Data class representing the statistics of a [Category].
 *
 * Used as output in some queries.
 *
 * @property category the [Category] associated with the statistics.
 * @property totalPOIs the total number of points of interest in the category.
 * @property visitedCount the number of points of interest visited in the category.
 */
@Serializable
data class CategoryStats(
    @Embedded val category: Category,
    val totalPOIs: Int,
    val visitedCount: Int
)