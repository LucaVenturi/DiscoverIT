package it.unibo.discoverit.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import kotlinx.serialization.Serializable

/**
 * Represents a friendship between two users.
 *
 * The friendship is more similar to a "follow-relationship" in social networks.
 * A [User] can follow another [User] but the other way around isn't guaranteed.
 * It's up to the other [User].
 *
 * This entity is stored in the `friendships` table.
 *
 * @property userId the id of the [User] that is following the other [User].
 * @property friendId the id of the [User] that is followed by the other [User].
 * @property friendshipDate the date when the friendship was created.
 */
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