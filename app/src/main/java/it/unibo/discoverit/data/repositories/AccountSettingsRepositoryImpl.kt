package it.unibo.discoverit.data.repositories

import android.graphics.Bitmap
import it.unibo.discoverit.data.database.entities.User
import it.unibo.discoverit.utils.profilepic.ProfilePicStorageHelper

/**
 * Implementation of [AccountSettingsRepository] that accesses the local Room database.
 *
 * @property userRepository the repository for user-related operations.
 * @property profilePicStorageHelper the helper for saving and loading profile pictures.
 */
class AccountSettingsRepositoryImpl(
    private val userRepository: UserRepository,
    private val profilePicStorageHelper: ProfilePicStorageHelper
): AccountSettingsRepository {
    override suspend fun updateProfilePicture(userId: Long, bitmap: Bitmap): User {
        val filename = "pp_$userId.jpg"
        val path = profilePicStorageHelper.save(bitmap, filename)

        val user = userRepository.updateProfilePicture(userId, path)

        return user
    }

    override suspend fun changeUsername(userId: Long, newUsername: String) {
        if (newUsername.isBlank())
            throw IllegalArgumentException("Username cannot be empty")
        val user = userRepository.getUserById(userId)
        if (user.username == newUsername)
            throw IllegalArgumentException("Username cannot be the same as the old one")
        val updatedUser = user.copy(username = newUsername)
        userRepository.update(updatedUser)
    }
}