package it.unibo.discoverit.data.database.entities

import android.net.Uri
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import androidx.core.net.toUri

/**
 * Represents a user of the application.
 * Stores the user's basic profile information along with a hashed password and some optional
 * parameters for the profile picture.
 *
 * This entity is stored in the `users` table.
 *
 * @property userId unique identifier of the [User].
 * @property username display name of the [User].
 * @property hashedPassword hashed password of the [User].
 * @property profilePicPath path to the profile picture of the [User].
 * @property profilePicLastModified last time the profile picture was modified.
 * Used to invalidate the cache, forces the the app to recognise the pic has changed.
 */
@Serializable
@Entity(
    tableName = "users",
    indices = [Index(value = ["username"], unique = true)]
)
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Long = 0,
    val username: String,
    val hashedPassword: String,
    val profilePicPath: String?,
    val profilePicLastModified: Long?
) {
    /**
     * Returns the [Uri] of the profile picture of the [User].
     * If the [profilePicPath] is null or blank, returns null.
     */
    @get:Ignore
    val profilePicUri: Uri?
        get() = profilePicPath
            ?.takeIf { it.isNotBlank() }
            ?.let { "file://${it}?t=$profilePicLastModified".toUri() }
}

