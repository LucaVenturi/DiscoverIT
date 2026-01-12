package it.unibo.discoverit.data.repositories

import it.unibo.discoverit.data.database.entities.CategoryStats
import kotlinx.coroutines.flow.Flow
import it.unibo.discoverit.data.database.entities.User
import it.unibo.discoverit.data.database.entities.Category


/**
 * Repository for category-related operations.
 */
interface CategoryRepository {
    /**
     * Retrieves the [category stats][CategoryStats] for the given [userId] and for each [Category]
     *
     * @param userId the id of the [User] whose category stats are to be retrieved.
     * @return a flow emitting a list of [CategoryStats] for the given [userId].
     */
    fun getCategoriesWithStats(userId: Long): Flow<List<CategoryStats>>

    /**
     * Retrieves the name of the category with the given [categoryId].
     *
     * @param categoryId the id of the [Category] whose name is to be retrieved.
     * @return the name of the [Category] with the given [categoryId].
     */
    suspend fun getCategoryName(categoryId: Long): String
}