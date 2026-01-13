package it.unibo.discoverit.ui.screens.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessStarted
import androidx.navigation.NavHostController
import it.unibo.discoverit.R
import it.unibo.discoverit.ui.composables.DiscoverItTopAppBar
import it.unibo.discoverit.ui.composables.ErrorMessage
import it.unibo.discoverit.ui.screens.account.composables.ChangeUsernameSection
import it.unibo.discoverit.ui.screens.account.composables.LogoutAndDeleteAccountSection
import it.unibo.discoverit.ui.screens.account.composables.ProfilePicSection
import it.unibo.discoverit.ui.screens.account.composables.SelectImageSourceDialog
import it.unibo.discoverit.ui.screens.login.UserState
import it.unibo.discoverit.utils.images.rememberCameraLauncher
import it.unibo.discoverit.utils.images.rememberGalleryLauncher
import it.unibo.discoverit.utils.images.uriToBitmap

/**
 * Composable for displaying the account settings screen.
 *
 * @param navController The navigation controller for handling navigation.
 * @param state The state of the account settings screen.
 * @param actions The actions for the account settings screen.
 * @param userState The state of the user.
 * @param onLogout The action to perform when the user logs out.
 */
@Composable
fun AccountSettingsScreen(
    navController: NavHostController,
    state: AccountSettingsState,
    actions: AccountSettingsActions,
    userState: UserState,
    onLogout: () -> Unit
) {
    // Context needed to get the content resolver
    val ctx = LocalContext.current

    // Launcher for picking an image from the gallery
    val galleryLauncher = rememberGalleryLauncher(
        onPicturePicked = { imageUri ->
            actions.onImagePicked(uriToBitmap(imageUri, ctx.contentResolver))
        }
    )

    // Launcher for taking a picture with the camera
    val cameraLauncher = rememberCameraLauncher(
        onPictureTaken = { imageUri ->
            actions.onImagePicked(uriToBitmap(imageUri, ctx.contentResolver))
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            DiscoverItTopAppBar(navController, stringResource(R.string.settings))
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            // Show the dialog to pick an image source if the flag is set
            if (state.showImageSourceDialog){
                SelectImageSourceDialog(
                    galleryLauncher = galleryLauncher,
                    cameraLauncher = cameraLauncher,
                    onPickFromGallery = actions::onPickFromGallery,
                    onTakePhoto = actions::onTakePhoto,
                    onDismiss = actions::onDismissImageSourceDialog
                )
            }

            // Show the dialog to confirm the logout if the flag is set
            if (state.showLogoutDialog) {
                ConfirmDialog(
                    label = stringResource(R.string.logout_confirm_dialog),
                    onDismissRequest = actions::onLogoutDismiss,
                    onConfirmation = {
                        actions.onLogoutConfirmation()
                        onLogout()
                    }
                )
            }

            // Show the dialog to confirm the deletion of the account if the flag is set
            if (state.showDeleteAccountDialog) {
                ConfirmDialog(
                    label = stringResource(R.string.delete_account_confirm_dialog),
                    onDismissRequest = actions::onDeleteAccountDismiss,
                    onConfirmation = {
                        actions.onDeleteAccountConfirmation()
                        onLogout()
                    }
                )
            }

            // Show the section to change the profile picture
            ProfilePicSection(
                profilePicPath = userState.user?.profilePicUri,
                onChangeProfilePicClick = actions::onChangeProfilePicClick
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Show the section to change the username
            ChangeUsernameSection(
                shownUsername = state.username,
                onUsernameChange = actions::onUsernameChange,
                isUsernameChanged = state.isUsernameChanged,
                onSaveClick = actions::onSaveClick
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Show the buttons to logout or delete the account.
            LogoutAndDeleteAccountSection(
                onLogoutClick = actions::onLogoutClick,
                onDeleteAccountClick = actions::onDeleteAccountClick
            )

            state.errorMsg?.let {
                ErrorMessage(it)
            }
        }
    }
}

/**
 * Custom dialog for confirming an action.
 *
 * @param label The label for the dialog.
 * @param onDismissRequest The action to perform when the dialog is dismissed.
 * @param onConfirmation The action to perform when the user confirms the action.
 */
@Composable
private fun ConfirmDialog(
    label: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = stringResource(R.string.warning)
            )
        },
        title = {
            Text(stringResource(R.string.warning))
        },
        text = {
            Text(label)
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = dropUnlessStarted { onConfirmation() }
            ) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = dropUnlessStarted { onDismissRequest() }
            ) {
                Text(stringResource(R.string.dismiss))
            }
        }
    )
}