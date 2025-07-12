package it.unibo.discoverit.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.unibo.discoverit.data.database.entities.PointOfInterest

@Dao
interface PointOfInterestDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pois: List<PointOfInterest>)

    @Query(
        "SELECT * FROM points_of_interest WHERE categoryId = :categoryId"
    )
    suspend fun getAllByCategory(categoryId: Long): List<PointOfInterest>

    @Query(
        "SELECT * FROM points_of_interest WHERE poiId = :poiId"
    )
    suspend fun getById(poiId: Long): PointOfInterest?

    @Query("SELECT COUNT(*) FROM points_of_interest WHERE categoryId = :categoryId")
    suspend fun getCountByCategory(categoryId: Long): Int

    @Query(
        "SELECT COUNT(*) FROM points_of_interest " +
        "WHERE categoryId = :categoryId AND poiId IN " +
        "(SELECT poiId FROM visits WHERE userId = :userId)"
    )
    fun getCountVisitedByCategory(userId: Long, categoryId: Long): Int

}