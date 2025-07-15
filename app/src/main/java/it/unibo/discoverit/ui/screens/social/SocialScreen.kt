package it.unibo.discoverit.ui.screens.social

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
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
        topBar = { MyTopAppBar(navController) },
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
                        state = state,
                        onConfirm = { actions.onConfirmAddFriendDialog(it) },
                        onDismiss = { actions.onDismissAddFriendDialog() },
                        onUsernameChange = { actions.onUsernameChange(it) }
                    )
                }
                if (state.showRemoveFriendDialog) {
                    ConfirmRemoveFriendDialog(
                        state = state,
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

@Composable
fun FriendCard(
    friend: User,
    countCompleted: Long,
    onClick: (Long) -> Unit,
    onLongPress: (User) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable() {
                onClick(friend.userId)
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onLongPress(friend) },
                )
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp),
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

@Composable
fun AddFriendDialog(
    state: SocialState,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    onUsernameChange: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Aggiungi amico") },
        text = {
            Column {
                Text("Inserisci l'username dell'amico:")
                OutlinedTextField(
                    value = state.usernameToAdd,
                    onValueChange = onUsernameChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Username") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(state.usernameToAdd) },
                enabled = state.usernameToAdd.isNotBlank()
            ) {
                Text("Conferma")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Annulla")
            }
        }
    )
}

@Composable
fun ConfirmRemoveFriendDialog(
    state: SocialState,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Rimuovi amico") },
        text = {
            Text("Rimuovere ${state.selectedFriendForRemoval?.username} dagli amici?")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Rimuovi")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Annulla")
            }
        }
    )
}
