package it.unibo.discoverit.ui.screens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import it.unibo.discoverit.ui.composables.MyTopAppBar
import it.unibo.discoverit.ui.screens.login.UserState
import it.unibo.discoverit.utils.images.ImageSourceLauncher
import it.unibo.discoverit.utils.images.rememberCameraLauncher
import it.unibo.discoverit.utils.images.rememberGalleryLauncher
import it.unibo.discoverit.utils.images.uriToBitmap

@Composable
fun AccountSettingsScreen(
    navController: NavHostController,
    state: AccountSettingsState,
    actions: AccountSettingsActions,
    userState: UserState
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
            MyTopAppBar(navController, "Impostazioni")
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
                    actions = actions,
                    galleryLauncher = galleryLauncher,
                    cameraLauncher = cameraLauncher
                )
            }

            ProfilePicSection(state = state, actions = actions, userState)

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            ChangeUsernameSection(
                actions = actions,
                state = state
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            LogoutAndDeleteAccountSection()
        }
    }
}

@Composable
fun ProfilePicSection(state: AccountSettingsState, actions: AccountSettingsActions, userState: UserState) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(userState.user?.profilePicPath)
                .crossfade(true)
                .build(),
            contentDescription = "Foto profilo",
            contentScale = ContentScale.Crop,
            placeholder = rememberVectorPainter(Icons.Default.AccountCircle),
            error = rememberVectorPainter(Icons.Default.AccountCircle),
            fallback = rememberVectorPainter(Icons.Default.AccountCircle),
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
        )
    }

    Spacer(Modifier.height(8.dp))

    TextButton(
        onClick = { actions.onChangeProfilePicClick() }
    ) {
        Text("Cambia foto profilo")
    }
}

@Composable
fun ChangeUsernameSection(
    actions: AccountSettingsActions,
    state: AccountSettingsState
) {
    // Cambia nome utente
    Text(
        text = "Nome utente",
        style = MaterialTheme.typography.titleMedium
    )

    Spacer(Modifier.height(4.dp))

    Row {
        OutlinedTextField(
            value = state.username,
            onValueChange = { actions.onUsernameChange(it) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            trailingIcon = {
                SaveUsernameChangesIconButton(
                    usernameChanged = state.isUsernameChanged,
                    onClick = actions::onSaveClick
                )
            }
        )
    }
}

@Composable
fun SaveUsernameChangesIconButton(
    usernameChanged: Boolean,
    onClick: () -> Unit = {}
) {
    if (usernameChanged) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Salva",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun LogoutAndDeleteAccountSection(

){
    Button(
        onClick = {/* TODO() */},
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Logout")
    }

    Spacer(Modifier.height(8.dp))

    OutlinedButton(
        onClick = {/* TODO() */},
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.error
        )
    ) {
        Text("Elimina account")
    }
}

@Composable
fun SelectImageSourceDialog(
    actions: AccountSettingsActions,
    galleryLauncher: ImageSourceLauncher = rememberGalleryLauncher(),
    cameraLauncher: ImageSourceLauncher = rememberCameraLauncher()
) {
    AlertDialog(
        onDismissRequest = { actions.onDismissImageSourceDialog() },
        title = { Text("Seleziona la fonte dell'immagine") },
        text = { Text("Da dove vuoi prendere la foto profilo?") },
        confirmButton = {
            TextButton(onClick = {
                actions.onPickFromGallery()
                galleryLauncher.captureImage()
            }) {
                Text("Dalla Galleria")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                actions.onTakePhoto()
                cameraLauncher.captureImage()
            }) {
                Text("Scatta una foto con la Camera")
            }
        }
    )
}