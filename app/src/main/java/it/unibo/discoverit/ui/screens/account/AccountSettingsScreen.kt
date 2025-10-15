package it.unibo.discoverit.ui.screens.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.unibo.discoverit.ui.composables.DiscoverItTopAppBar
import it.unibo.discoverit.ui.screens.account.composables.ChangeUsernameSection
import it.unibo.discoverit.ui.screens.account.composables.LogoutAndDeleteAccountSection
import it.unibo.discoverit.ui.screens.account.composables.ProfilePicSection
import it.unibo.discoverit.ui.screens.account.composables.SelectImageSourceDialog
import it.unibo.discoverit.ui.screens.login.UserState
import it.unibo.discoverit.utils.images.rememberCameraLauncher
import it.unibo.discoverit.utils.images.rememberGalleryLauncher
import it.unibo.discoverit.utils.images.uriToBitmap

@Composable
fun AccountSettingsScreen(
    navController: NavHostController,
    state: AccountSettingsState,
    actions: AccountSettingsActions,
    userState: UserState,
    onLogout: () -> Unit
) {
    val ctx = LocalContext.current

    val galleryLauncher = rememberGalleryLauncher(
        onPicturePicked = { imageUri ->
            actions.onImagePicked(uriToBitmap(imageUri, ctx.contentResolver))
        }
    )

    val cameraLauncher = rememberCameraLauncher(
        onPictureTaken = { imageUri ->
            actions.onImagePicked(uriToBitmap(imageUri, ctx.contentResolver))
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            DiscoverItTopAppBar(navController, "Settings")
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (state.showImageSourceDialog){
                SelectImageSourceDialog(
                    galleryLauncher = galleryLauncher,
                    cameraLauncher = cameraLauncher,
                    onPickFromGallery = actions::onPickFromGallery,
                    onTakePhoto = actions::onTakePhoto,
                    onDismiss = actions::onDismissImageSourceDialog
                )
            }

            if (state.showLogoutDialog) {
                ConfirmDialog(
                    label = "Are you sure you want to logout?",
                    onDismissRequest = actions::onLogoutDismiss,
                    onConfirmation = {
                        actions.onLogoutConfirmation()
                        onLogout()
                    }
                )
            }

            if (state.showDeleteAccountDialog) {
                ConfirmDialog(
                    label = "Are you sure you want to delete your account?\nThis action cannot be undone.",
                    onDismissRequest = actions::onDeleteAccountDismiss,
                    onConfirmation = {
                        actions.onDeleteAccountConfirmation()
                        onLogout()
                    }
                )
            }

            ProfilePicSection(userState.user?.profilePicPath, actions::onChangeProfilePicClick)

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            ChangeUsernameSection(
                shownUsername = state.username,
                onUsernameChange = actions::onUsernameChange,
                isUsernameChanged = state.isUsernameChanged,
                onSaveClick = actions::onSaveClick
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            LogoutAndDeleteAccountSection(actions::onLogoutClick, actions::onDeleteAccountClick)
        }
    }
}

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
                contentDescription = "Warning"
            )
        },
        title = {
            Text("Caution")
        },
        text = {
            Text(label)
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onConfirmation
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("Dismiss")
            }
        }
    )
}