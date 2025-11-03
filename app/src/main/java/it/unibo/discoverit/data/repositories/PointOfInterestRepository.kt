package it.unibo.discoverit.data.repositories

import it.unibo.discoverit.data.database.entities.PointOfInterest

interface PointOfInterestRepository {
    suspend fun getPOIsByCategory(categoryId: Long): List<PointOfInterest>
    suspend fun getPOIDetails(poiId: Long): PointOfInterest?
    suspend fun toggleVisit(userId: Long, poiId: Long)
    suspend fun isVisited(userId: Long, poiId: Long): Boolean
}