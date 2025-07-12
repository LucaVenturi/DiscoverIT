package it.unibo.discoverit.data.repositories

import it.unibo.discoverit.data.database.daos.PointOfInterestDAO
import it.unibo.discoverit.data.database.daos.VisitDAO
import it.unibo.discoverit.data.database.entities.PointOfInterest
import it.unibo.discoverit.data.database.entities.Visit

class PointOfInterestRepository(
    private val poiDAO: PointOfInterestDAO,
    private val visitDAO: VisitDAO
) {
    suspend fun getPOIsByCategory(categoryId: Long): List<PointOfInterest> {
        return poiDAO.getAllByCategory(categoryId)
    }

    suspend fun getPOIDetails(poiId: Long): PointOfInterest? {
        return poiDAO.getById(poiId)
    }

    suspend fun toggleVisit(userId: Long, poiId: Long) {
        val existingVisit = visitDAO.getVisit(userId, poiId)
        if (existingVisit == null) {
            visitDAO.insert(Visit(userId, poiId, System.currentTimeMillis()))
        } else {
            visitDAO.delete(existingVisit)
        }
    }

    suspend fun isVisited(userId: Long, poiId: Long): Boolean {
        return visitDAO.getVisit(userId, poiId) != null
    }
}