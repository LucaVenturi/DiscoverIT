package it.unibo.discoverit.data.database.entities

import androidx.room.Entity
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
    val profilePicPath: String?
)