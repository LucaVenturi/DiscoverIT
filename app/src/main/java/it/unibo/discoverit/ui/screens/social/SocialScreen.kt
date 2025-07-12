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
import it.unibo.discoverit.Destination
import it.unibo.discoverit.ui.composables.DiscoverItNavigationBar
import it.unibo.discoverit.ui.composables.MyTopAppBar

@Composable
fun SocialScreen(
    navController: NavHostController
) {
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = { MyTopAppBar(navController) },
        bottomBar = {
            DiscoverItNavigationBar(
                currentRoute = Destination.Social,
                onNavigateTo = { /*TODO*/ }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
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
                    onClick = { navController.navigate(Destination.UserDetail) }
                )

                Text(
                    text = "Amici",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                FriendsList(
                    friends = listOf("Marco", "Luca", "Anna", "Antonio", "Giovanni"),
                    onFriendClick = { navController.navigate(Destination.UserDetail) }
                )
            }
        }
    }
}

@Composable
fun UserProfileSection(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable { onClick() },
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
                    text = "currentUser.username",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "currentUser.visitedPoints visitati",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun FriendsList(friends: List<String>, onFriendClick: () -> Unit) {
    LazyColumn {
        items(friends) { friend ->
            FriendCard(
                friendName = friend,
                onClick = onFriendClick
            )
        }
    }
}

@Composable
fun FriendCard(friendName: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
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
                    text = friendName,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "friend.visitedPoints visitati",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


