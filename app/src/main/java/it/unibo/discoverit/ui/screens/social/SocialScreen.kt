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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import it.unibo.discoverit.BottomNavDestination
import it.unibo.discoverit.Destination
import it.unibo.discoverit.data.database.entities.User
import it.unibo.discoverit.ui.composables.DiscoverItNavigationBar
import it.unibo.discoverit.ui.composables.DiscoverItTopAppBar
import it.unibo.discoverit.ui.screens.social.composables.FriendCard
import it.unibo.discoverit.ui.screens.home.composables.AddFriendDialog
import it.unibo.discoverit.ui.screens.home.composables.ConfirmRemoveFriendDialog
import it.unibo.discoverit.ui.screens.login.UserState

@Composable
fun SocialScreen(
    navController: NavHostController,
    state: SocialState,
    actions: SocialActions,
    userState: UserState,
    onNavigateTo: (BottomNavDestination) -> Unit,
    onUserClick: (Long) -> Unit,
) {
    // Crea e gestisci lo stato della Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    // Mostra la Snackbar quando lo stato cambia
    LaunchedEffect(state.showSnackbar, state.snackbarMessage) {
        if (state.showSnackbar && state.snackbarMessage != null) {
            snackbarHostState.showSnackbar(
                message = state.snackbarMessage,
                actionLabel = "OK"
            )
            // Resetta lo stato dopo la visualizzazione
            actions.onSnackbarDismiss()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = { DiscoverItTopAppBar(navController) },
        bottomBar = {
            DiscoverItNavigationBar(
                currentRoute = Destination.Social,
                onNavigateTo = onNavigateTo
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { actions.onAddFriendClick() },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Aggiungi amico")
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
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
                    onFriendClick = onUserClick,
                    onFriendLongPress = { actions.onFriendLongPress(it) }
                )

                if (state.isAddFriendDialogVisible) {
                    AddFriendDialog(
                        usernameToAdd = state.usernameToAdd,
                        onConfirm = { actions.onConfirmAddFriendDialog(it) },
                        onDismiss = { actions.onDismissAddFriendDialog() },
                        onUsernameChange = { actions.onUsernameChange(it) }
                    )
                }
                if (state.showRemoveFriendDialog && state.selectedFriendForRemoval != null) {
                    ConfirmRemoveFriendDialog(
                        username = state.selectedFriendForRemoval.username,
                        onDismiss = { actions.onDismissRemoveFriendDialog() },
                        onConfirm = { actions.onConfirmRemoveFriend() }
                    )
                }
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
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(currentUser.profilePicUri)
                    .crossfade(true)
                    .build(),
                contentDescription = "Foto profilo",
                contentScale = ContentScale.Crop,
                placeholder = rememberVectorPainter(Icons.Default.Person),
                error = rememberVectorPainter(Icons.Default.Person),
                fallback = rememberVectorPainter(Icons.Default.Person),
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = currentUser.username,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "$countCompleted traguardi raggiunti",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun FriendsList(
    friendsAndCountCompleted: Map<User, Long>,
    onFriendClick: (Long) -> Unit,
    onFriendLongPress: (User) -> Unit
) {
    LazyColumn {
        items(friendsAndCountCompleted.toList()) { friendAndCount ->
            FriendCard(
                friend = friendAndCount.first,
                countCompleted = friendAndCount.second,
                onClick = onFriendClick,
                onLongPress = onFriendLongPress
            )
        }
    }
}
