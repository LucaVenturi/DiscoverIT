package it.unibo.discoverit.data.repositories

import it.unibo.discoverit.data.database.daos.CategoryDAO
import it.unibo.discoverit.data.database.daos.PointOfInterestDAO
import it.unibo.discoverit.data.database.entities.CategoryStats
import kotlinx.coroutines.flow.Flow

class CategoryRepository(
    private val categoryDAO: CategoryDAO,
    private val poiDAO: PointOfInterestDAO
) {
    fun getCategoriesWithStats(userId: Long): Flow<List<CategoryStats>> {
        return categoryDAO.getCategoriesWithStats(userId)
    }

//    suspend fun getCategoryById(categoryId: Long): Category? {
//        return categoryDao.getById(categoryId)
//    }
}