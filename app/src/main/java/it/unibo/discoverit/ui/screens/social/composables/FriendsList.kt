package it.unibo.discoverit.ui.screens.social.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import it.unibo.discoverit.data.database.entities.User

@Composable
fun FriendsList(
    friendsAndCountCompleted: Map<User, Long>,
    onFriendClick: (Long) -> Unit,
    onFriendLongPress: (User) -> Unit
) {
    // Non uso una lazycolumn perchè improbabile avere tantissimi amici e per non creare due zone scrollabili nella stessa pagina.
    Column {
        friendsAndCountCompleted.toList().forEach { friendAndCount ->
            FriendCard(
                friend = friendAndCount.first,
                countCompleted = friendAndCount.second,
                onClick = onFriendClick,
                onLongPress = onFriendLongPress
            )
        }
    }
}