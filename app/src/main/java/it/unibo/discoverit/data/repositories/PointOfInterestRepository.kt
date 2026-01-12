package it.unibo.discoverit.data.repositories

import it.unibo.discoverit.data.database.entities.PointOfInterest
import it.unibo.discoverit.data.database.entities.Category
import it.unibo.discoverit.data.database.entities.User



/**
 * Repository for point of interest-related operations.
 * @see PointOfInterest
 */
interface PointOfInterestRepository {
    /**
     * Retrieves all the [points of interest][PointOfInterest] for the given [categoryId].
     *
     * @param categoryId the id of the [category][Category] for which to retrieve the
     * [points of interest][PointOfInterest].
     * @return a list of all the [points of interest][PointOfInterest] for the given [categoryId].
     */
    suspend fun getPOIsByCategory(categoryId: Long): List<PointOfInterest>

    /**
     * Retrieves the details of a [point of interest][PointOfInterest] with the given [poiId].
     *
     * @param poiId the id of the [point of interest][PointOfInterest] whose details are to be
     * retrieved.
     * @return the [PointOfInterest] with the given [poiId], null if not found.
     */
    suspend fun getPOIDetails(poiId: Long): PointOfInterest?

    /**
     * Toggles the visit status of a [point of interest][PointOfInterest] for the given [userId].
     *
     * @param userId the id of the [user][User] whose visit status is to be toggled.
     * @param poiId the id of the [point of interest][PointOfInterest] whose visit status is to be
     * toggled.
     */
    suspend fun toggleVisit(userId: Long, poiId: Long)

    /**
     * Checks if a [point of interest][PointOfInterest] has been visited by the given [userId].
     *
     * @param userId the id of the [user][User] whose visit status is to be checked.
     * @param poiId the id of the [point of interest][PointOfInterest] whose visit status is to be
     * checked.
     * @return true if the [point of interest][PointOfInterest] has been visited by the
     * [user][User], false otherwise.
     */
    suspend fun isVisited(userId: Long, poiId: Long): Boolean
}