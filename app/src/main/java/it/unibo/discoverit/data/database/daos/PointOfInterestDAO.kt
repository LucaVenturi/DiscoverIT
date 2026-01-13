package it.unibo.discoverit.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.unibo.discoverit.data.database.entities.PointOfInterest
import it.unibo.discoverit.data.database.entities.Category


/**
 * Data Access Object for the [PointOfInterest] entity.
 * Contains methods to interact with the [PointOfInterest] table in the database.
 */
@Dao
interface PointOfInterestDAO {
    /**
     * Inserts a list of [PointOfInterest] into the database. If the [PointOfInterest] already
     * exists, it will be replaced.
     * @param pois the list of [PointOfInterest] to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pois: List<PointOfInterest>)

    /**
     * Retrieves all the [points of interest][PointOfInterest] from the database,
     * filtered by [Category].
     *
     * @param categoryId the id of the [Category] to filter by.
     * @return the list of all [points of interest][PointOfInterest] of that [category][Category].
     */
    @Query(
        "SELECT * FROM points_of_interest WHERE categoryId = :categoryId"
    )
    suspend fun getAllByCategory(categoryId: Long): List<PointOfInterest>

    /**
     * Retrieves the [PointOfInterest] with the given [poiId] from the database.
     *
     * @param poiId the id of the [PointOfInterest] to retrieve.
     */
    @Query(
        "SELECT * FROM points_of_interest WHERE poiId = :poiId"
    )
    suspend fun getById(poiId: Long): PointOfInterest?

    /**
     * Retrieves the number of [points of interest][PointOfInterest] in that [category][Category].
     *
     * @param categoryId the id of the [Category] to filter by.
     * @return the number of [points of interest][PointOfInterest] in the requested
     * [category][Category].
     */
    @Query("SELECT COUNT(*) FROM points_of_interest WHERE categoryId = :categoryId")
    suspend fun getCountByCategory(categoryId: Long): Int

//    /**
//     * Gets the number of visited [points of interest][PointOfInterest] in a [category][Category]
//     * by a given [user][User].
//     *
//     * @param userId the id of the [User] to filter by.
//     * @param categoryId the id of the [Category] to filter by.
//     * @return the number of visited [points of interest][PointOfInterest] in the requested
//     * [category][Category] by the given [user][User].
//     */
//    @Query(
//        "SELECT COUNT(*) FROM points_of_interest " +
//        "WHERE categoryId = :categoryId AND poiId IN " +
//        "(SELECT poiId FROM visits WHERE userId = :userId)"
//    )
//    fun getCountVisitedByCategory(userId: Long, categoryId: Long): Int

}