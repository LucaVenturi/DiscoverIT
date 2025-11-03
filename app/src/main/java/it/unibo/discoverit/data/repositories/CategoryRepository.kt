package it.unibo.discoverit.data.repositories

import it.unibo.discoverit.data.database.entities.CategoryStats
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategoriesWithStats(userId: Long): Flow<List<CategoryStats>>
    suspend fun getCategoryName(categoryId: Long): String
}