package it.unibo.discoverit.data.repositories

import android.graphics.Bitmap
import it.unibo.discoverit.data.database.entities.User

interface AccountSettingsRepository {
    suspend fun updateProfilePicture(userId: Long, bitmap: Bitmap): User
    suspend fun changeUsername(userId: Long, newUsername: String)
}