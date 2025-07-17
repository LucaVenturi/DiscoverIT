package it.unibo.discoverit.data.repositories

import android.graphics.Bitmap
import it.unibo.discoverit.data.database.entities.User
import it.unibo.discoverit.utils.profilepic.ProfilePicStorageHelper

class AccountSettingsRepository(
    private val userRepository: UserRepository,
    private val profilePicStorageHelper: ProfilePicStorageHelper
) {
    suspend fun updateProfilePicture(userId: Long, bitmap: Bitmap): String {
        val filename = "profile_$userId.jpg"
        val path = profilePicStorageHelper.save(bitmap, filename)

        val user = userRepository.getUserById(userId)
        val updatedUser = user.copy(profilePicPath = path)
        userRepository.update(updatedUser)

        return path
    }
}