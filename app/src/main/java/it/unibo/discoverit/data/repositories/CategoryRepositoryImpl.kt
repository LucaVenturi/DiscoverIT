package it.unibo.discoverit.data.repositories

import it.unibo.discoverit.data.database.daos.CategoryDAO
import it.unibo.discoverit.data.database.entities.CategoryStats
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of [CategoryRepository] that retrieves data from a Room database.
 *
 * @property categoryDAO DAO for category-related database operations.
 */
class CategoryRepositoryImpl(
    private val categoryDAO: CategoryDAO
): CategoryRepository {
    override fun getCategoriesWithStats(userId: Long): Flow<List<CategoryStats>> {
        return categoryDAO.getCategoriesWithStats(userId)
    }

    override suspend fun getCategoryName(categoryId: Long): String {
        return categoryDAO.getCategoryName(categoryId)
    }

//    suspend fun getCategoryById(categoryId: Long): Category? {
//        return categoryDao.getById(categoryId)
//    }
}