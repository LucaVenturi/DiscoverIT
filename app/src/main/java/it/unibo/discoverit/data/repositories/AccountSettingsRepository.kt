package it.unibo.discoverit.data.repositories

import android.graphics.Bitmap
import it.unibo.discoverit.data.database.entities.User

/**
 * Repository for account settings related operations.
 */
interface AccountSettingsRepository {
    /**
     * Updates the profile picture of the user with the given [userId] to the given [bitmap].
     *
     * @param userId the id of the user whose profile picture is to be updated.
     * @param bitmap the new profile picture of the user.
     */
    suspend fun updateProfilePicture(userId: Long, bitmap: Bitmap): User

    /**
     * Changes the username of the user with the given [userId] to the given [newUsername].
     *
     * @param userId the id of the user whose username is to be changed.
     * @param newUsername the new username of the user
     */
    suspend fun changeUsername(userId: Long, newUsername: String)
}