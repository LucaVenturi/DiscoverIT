package it.unibo.discoverit.data.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.unibo.discoverit.data.database.entities.Visit
import it.unibo.discoverit.data.database.entities.User
import it.unibo.discoverit.data.database.entities.PointOfInterest


/**
 * Data Access Object for the [Visit] entity.
 * Contains methods to interact with the [visits][Visit].
 */
@Dao
interface VisitDAO {
    /**
     * Inserts a list of [Visit] into the database. If the [Visit] already exists,
     * it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(visits: List<Visit>)

    /**
     * Retrieves the [Visit] with the given [userId] and [poiId] from the database.
     * If it doesn't exist, returns null.
     *
     * @param userId the id of the [User] who visited the [PointOfInterest].
     */
    @Query("SELECT * FROM visits WHERE userId = :userId AND poiId = :poiId")
    suspend fun getVisit(userId: Long, poiId: Long): Visit?

    /**
     * Inserts a [Visit] into the database.
     * If the [Visit] already exists, it will be replaced.
     *
     * @param visit the [Visit] to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(visit: Visit)

    /**
     * Deletes a [Visit] from the database.
     *
     * @param visit the [Visit] to delete.
     */
    @Delete
    suspend fun delete(visit: Visit)

    /**
     * Retrieves if the [User] with the given [userId] has visited the [PointOfInterest] with the
     * given [poiId].
     *
     * @param userId the id of the [User] who visited the [PointOfInterest].
     * @param poiId the id of the [PointOfInterest] visited by the [User].
     * @return true if the [User] has visited the [PointOfInterest], false otherwise.
     */
    @Query("""
        SELECT EXISTS (
            SELECT 1 FROM visits
            WHERE userId = :userId AND poiId = :poiId
        )
    """)
    suspend fun isPOIVisited(userId: Long, poiId: Long): Boolean
}