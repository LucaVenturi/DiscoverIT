package it.unibo.discoverit.ui.screens.account

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import it.unibo.discoverit.Destination
import it.unibo.discoverit.ui.composables.MyTopAppBar
import java.io.File

@Composable
fun AccountScreen(
    navController: NavHostController,
    lastSelectedTab: Destination = Destination.Home /* todo remove */,
    state: AccountSettingsState,
    actions: AccountSettingsActions,
    onNavigateTo: (Destination) -> Unit
) {
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {  }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { /* TODO */ },
    )

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            MyTopAppBar(navController)
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
                SelectImageSourceDialog()
            }

            ProfilePicSection(actions = actions)

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            ChangeUsernameSection()

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            LogoutAndDeleteAccountSection()
        }
    }
}

@Composable
fun ProfilePicSection(actions: AccountSettingsActions) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Foto profilo",
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary
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
fun ChangeUsernameSection() {
    // Cambia nome utente
    Text(
        text = "Nome utente",
        style = MaterialTheme.typography.titleMedium
    )

    Spacer(Modifier.height(4.dp))

    Row {
        OutlinedTextField(
            value = "username",
            onValueChange = {/* TODO() */},
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            trailingIcon = {
                SaveUsernameChangesIconButton(
                    usernameChanged = false /* todo */
                )
            }
        )
    }
}

@Composable
fun SaveUsernameChangesIconButton(
    usernameChanged: Boolean
) {
    if (usernameChanged) {
        IconButton(onClick = {/* TODO() */ }) {
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

) {
    AlertDialog(
        onDismissRequest = {/* TODO() */},
        title = { Text("Seleziona la fonte dell'immagine") },
        text = { Text("Da dove vuoi prendere la foto profilo?") },
        confirmButton = {
            TextButton(onClick = {/* TODO() */}) {
                Text("Dalla Galleria")
            }
        },
        dismissButton = {
            TextButton(onClick = {/* TODO() */}) {
                Text("Scatta una foto con la Camera")
            }
        }
    )
}