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
import it.unibo.discoverit.ui.screens.home.composables.AddFriendDialog
import it.unibo.discoverit.ui.screens.home.composables.ConfirmRemoveFriendDialog
import it.unibo.discoverit.ui.screens.login.UserState
import it.unibo.discoverit.ui.screens.social.composables.AddFriendFab
import it.unibo.discoverit.ui.screens.social.composables.CurrentUserSection
import it.unibo.discoverit.ui.screens.social.composables.FriendsList
import it.unibo.discoverit.ui.screens.social.composables.FriendsSection

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
        topBar = {
            DiscoverItTopAppBar(navController)
        },
        bottomBar = {
            DiscoverItNavigationBar(
                currentRoute = Destination.Social,
                onNavigateTo = onNavigateTo
            )
        },
        floatingActionButton = {
            AddFriendFab(
                onClick = actions::onAddFriendClick,
                modifier = Modifier.padding(16.dp)
            )
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

                CurrentUserSection(
                    currentUser = userState.user ?: throw IllegalStateException("User is null"),
                    currentUserCountCompleted = state.currentUserCountCompleted,
                    onUserClick = onUserClick
                )

                FriendsSection(
                    friendsAndCountCompleted = state.friendsAndCountCompleted,
                    onUserClick = onUserClick,
                    onFriendLongPress = actions::onFriendLongPress,
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
