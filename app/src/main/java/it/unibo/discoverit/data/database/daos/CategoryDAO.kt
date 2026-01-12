package it.unibo.discoverit.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.unibo.discoverit.data.database.entities.Category
import it.unibo.discoverit.data.database.entities.CategoryStats
import kotlinx.coroutines.flow.Flow
import it.unibo.discoverit.data.database.entities.User
import it.unibo.discoverit.data.database.entities.PointOfInterest

/**
 * Data Access Object for the [Category] entity.
 * Contains methods to interact with the [Category] table in the database.
 */
@Dao
interface CategoryDAO {

    /**
     * Inserts a list of [Category] into the database. If the [Category] already exists,
     * it will be replaced.
     *
     * @param categories the list of [Category] to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<Category>)

    /**
     * Retrieves all the [categories][Category] from the database.
     * @return a flow emitting a list of all [categories][Category] from the database.
     */
    @Query("SELECT * FROM categories")
    fun getAll(): Flow<List<Category>>

    /**
     * Retrieves the [stats of the category][CategoryStats] for each [Category] for the given
     * [userId] from the database.
     * Includes the total number of points of interest in the category and the number of [points
     * of interest][PointOfInterest] visited by the [User].
     *
     * @param userId the id of the [User] to retrieve the stats for.
     * @return a flow emitting a list of [CategoryStats]
     */
    @Query("""
        SELECT 
            cat.*,
            COUNT(DISTINCT poi.poiId) AS totalPOIs,
            SUM(CASE WHEN vis.poiId IS NOT NULL THEN 1 ELSE 0 END) AS visitedCount
        FROM categories cat
        LEFT JOIN points_of_interest poi ON cat.categoryId = poi.categoryId
        LEFT JOIN visits vis ON (
            poi.poiId = vis.poiId 
            AND vis.userId = :userId
        )
        GROUP BY cat.categoryId
    """)
    fun getCategoriesWithStats(userId: Long): Flow<List<CategoryStats>>

    /**
     * Retrieves the name of the [Category] with the given [categoryId] from the database.
     *
     * @param categoryId the id of the [Category] to retrieve the name for.
     * @return the name of the [Category]
     */
    @Query("SELECT name FROM categories WHERE categoryId = :categoryId")
    suspend fun getCategoryName(categoryId: Long): String

    /**
     * Retrieves the number of [categories][Category] from the database.
     *
     * @return the number of [categories][Category]
     */
    @Query("SELECT COUNT(*) FROM categories")
    suspend fun getCategoriesCount(): Int
}