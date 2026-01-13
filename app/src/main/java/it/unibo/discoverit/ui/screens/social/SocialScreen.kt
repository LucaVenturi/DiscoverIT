package it.unibo.discoverit.ui.screens.social

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.unibo.discoverit.BottomNavDestination
import it.unibo.discoverit.Destination
import it.unibo.discoverit.R
import it.unibo.discoverit.ui.composables.DiscoverItNavigationBar
import it.unibo.discoverit.ui.composables.DiscoverItTopAppBar
import it.unibo.discoverit.ui.screens.social.composables.AddFriendDialog
import it.unibo.discoverit.ui.screens.social.composables.ConfirmRemoveFriendDialog
import it.unibo.discoverit.ui.screens.login.UserState
import it.unibo.discoverit.ui.screens.social.composables.AddFriendFab
import it.unibo.discoverit.ui.screens.social.composables.CurrentUserSection
import it.unibo.discoverit.ui.screens.social.composables.FriendsSection
import androidx.compose.ui.res.stringResource

/**
 * Composable of the social screen.
 * Shows the current user and the friends of the user.
 * Allows to add friends and remove friends.
 *
 * @param navController The navigation controller, needed by the top app bar.
 * @param state The state of the screen.
 * @param actions The actions that can be performed on the screen.
 * @param userState The state of the user.
 * @param onNavigateTo The action to perform when the user clicks on a navigation item.
 * @param onUserClick The action to perform when the user clicks on a user.
 */
@Composable
fun SocialScreen(
    navController: NavHostController,
    state: SocialState,
    actions: SocialActions,
    userState: UserState,
    onNavigateTo: (BottomNavDestination) -> Unit,
    onUserClick: (Long) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Get the strings from the resources.
    val addSuccess = stringResource(R.string.friend_add_success)
    val removeSuccess = stringResource(R.string.friend_remove_success)
    val addError = stringResource(R.string.friend_add_error)
    val removeError = stringResource(R.string.friend_remove_error)

    // Show snackbar when the state changes and has a message.
    state.currentMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(
                message = when (message) {
                    SocialMessage.AddSuccess -> addSuccess
                    SocialMessage.RemoveSuccess -> removeSuccess
                    is SocialMessage.AddError -> addError + "\n" + message.errMsg
                    is SocialMessage.RemoveError -> removeError + "\n" + message.errMsg
                    is SocialMessage.GenericError -> message.errMsg
                },
                actionLabel = "OK",
            )
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
                    .verticalScroll(rememberScrollState())
            ) {

                CurrentUserSection(
                    currentUser = userState.user
                        ?: throw IllegalStateException("User is null"),
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
