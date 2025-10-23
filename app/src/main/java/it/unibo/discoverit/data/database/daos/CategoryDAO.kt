package it.unibo.discoverit.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.unibo.discoverit.data.database.entities.Category
import it.unibo.discoverit.data.database.entities.CategoryStats
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<Category>)

    @Query("SELECT * FROM categories")
    fun getAll(): Flow<List<Category>>

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

    @Query("SELECT name FROM categories WHERE categoryId = :categoryId")
    suspend fun getCategoryName(categoryId: Long): String

    @Query("SELECT COUNT(*) FROM categories")
    suspend fun getCategoriesCount(): Int
}