package it.unibo.discoverit.data.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.unibo.discoverit.data.database.entities.Visit

@Dao
interface VisitDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(visits: List<Visit>)

    @Query("SELECT * FROM visits WHERE userId = :userId AND poiId = :poiId")
    suspend fun getVisit(userId: Long, poiId: Long): Visit?

    @Insert
    suspend fun insert(visit: Visit)

    @Delete
    suspend fun delete(visit: Visit)

    @Query("SELECT COUNT(*) FROM visits WHERE userId = :userId AND poiId = :poiId")
    suspend fun isPOIVisited(userId: Long, poiId: Long): Boolean
}