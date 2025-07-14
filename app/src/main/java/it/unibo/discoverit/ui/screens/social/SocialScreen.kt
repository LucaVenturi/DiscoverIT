package it.unibo.discoverit.ui.screens.social

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.unibo.discoverit.BottomNavDestination
import it.unibo.discoverit.Destination
import it.unibo.discoverit.data.database.entities.User
import it.unibo.discoverit.ui.composables.DiscoverItNavigationBar
import it.unibo.discoverit.ui.composables.MyTopAppBar
import it.unibo.discoverit.ui.screens.login.UserState

@Composable
fun SocialScreen(
    navController: NavHostController,
    state: SocialState,
    actions: SocialActions,
    userState: UserState,
    onNavigateTo: (BottomNavDestination) -> Unit,
    onAddFriendClick: () -> Unit,
    onUserClick: (Long) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = { MyTopAppBar(navController) },
        bottomBar = {
            DiscoverItNavigationBar(
                currentRoute = Destination.Social,
                onNavigateTo = onNavigateTo
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddFriendClick,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Aggiungi amico")
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "I miei traguardi",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                UserProfileSection(
                    currentUser = userState.user ?: throw IllegalStateException("User is null"),
                    countCompleted = state.currentUserCountCompleted,
                    onClick = onUserClick
                )

                Text(
                    text = "Amici",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                FriendsList(
                    friendsAndCountCompleted = state.friendsAndCountCompleted,
                    onFriendClick = onUserClick
                )
            }
        }
    }
}

@Composable
fun UserProfileSection(currentUser: User, countCompleted: Long, onClick: (Long) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable { onClick(currentUser.userId) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = currentUser.username,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "$countCompleted traguardi raggiunti",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun FriendsList(friendsAndCountCompleted: Map<User, Long>, onFriendClick: (Long) -> Unit) {
    LazyColumn {
        items(friendsAndCountCompleted.toList()) { friendAndCount ->
            FriendCard(
                friend = friendAndCount.first,
                countCompleted = friendAndCount.second,
                onClick = onFriendClick
            )
        }
    }
}

@Composable
fun FriendCard(friend: User, countCompleted: Long, onClick: (Long) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick(friend.userId) },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = friend.username,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "$countCompleted traguardi raggiungi",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


