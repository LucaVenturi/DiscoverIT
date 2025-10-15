package it.unibo.discoverit.data.database.entities

import android.net.Uri
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

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
    @get:Ignore
    val profilePicUri: Uri?
        get() = profilePicPath
            ?.takeIf { it.isNotBlank() }
            ?.let { Uri.parse("file://${it}?t=$profilePicLastModified") }
}

