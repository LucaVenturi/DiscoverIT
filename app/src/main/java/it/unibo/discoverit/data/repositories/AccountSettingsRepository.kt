package it.unibo.discoverit.data.repositories

import android.graphics.Bitmap
import it.unibo.discoverit.data.database.entities.User
import it.unibo.discoverit.utils.profilepic.ProfilePicStorageHelper

class AccountSettingsRepository(
    private val userRepository: UserRepository,
    private val profilePicStorageHelper: ProfilePicStorageHelper
) {
    suspend fun updateProfilePicture(userId: Long, bitmap: Bitmap): User {
        val filename = "pp_$userId.jpg"
        val path = profilePicStorageHelper.save(bitmap, filename)

        val user = userRepository.updateProfilePicture(userId, path)

        return user
    }

    suspend fun changeUsername(userId: Long, newUsername: String) {
        if (newUsername.isBlank())
            throw Exception("Username cannot be empty")
        val user = userRepository.getUserById(userId)
        if (user.username == newUsername)
            throw Exception("Username cannot be the same as the old one")
        val updatedUser = user.copy(username = newUsername)
        userRepository.update(updatedUser)
    }
}