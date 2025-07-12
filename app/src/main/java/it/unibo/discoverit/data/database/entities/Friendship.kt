package it.unibo.discoverit.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "friendships",
    primaryKeys = ["userId", "friendId"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["friendId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("friendId")]
)
data class Friendship(
    val userId: Long,
    val friendId: Long,
    val friendshipDate: Long = System.currentTimeMillis()
)