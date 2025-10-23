package it.unibo.discoverit.data.repositories

import it.unibo.discoverit.data.database.daos.CategoryDAO
import it.unibo.discoverit.data.database.entities.CategoryStats
import kotlinx.coroutines.flow.Flow

class CategoryRepository(
    private val categoryDAO: CategoryDAO
) {
    suspend fun getCategoriesWithStats(userId: Long): Flow<List<CategoryStats>> {
        return categoryDAO.getCategoriesWithStats(userId)
    }

    suspend fun getCategoryName(categoryId: Long): String {
        return categoryDAO.getCategoryName(categoryId)
    }

//    suspend fun getCategoryById(categoryId: Long): Category? {
//        return categoryDao.getById(categoryId)
//    }
}