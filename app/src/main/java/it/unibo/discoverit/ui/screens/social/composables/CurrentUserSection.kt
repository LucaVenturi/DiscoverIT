package it.unibo.discoverit.ui.screens.social.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.unibo.discoverit.data.database.entities.User

@Composable
fun CurrentUserSection(
    currentUser: User,
    currentUserCountCompleted: Long,
    onUserClick: (Long) -> Unit
) {
    Text(
        text = "I miei traguardi",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(bottom = 16.dp)
    )

    CurrentUserCard(
        currentUser = currentUser,
        countCompleted = currentUserCountCompleted,
        onClick = onUserClick
    )
}