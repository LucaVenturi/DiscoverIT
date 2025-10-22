package it.unibo.discoverit.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "categories",
)
data class Category(
    @PrimaryKey(autoGenerate = true) val categoryId: Long = 0,
    val name: String,
    val iconName: String? = null
)

@Serializable
data class CategoryStats(
    @Embedded val category: Category,
    val totalPOIs: Int,
    val visitedCount: Int
)