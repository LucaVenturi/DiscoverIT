package it.unibo.discoverit.ui.screens.home.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AddFriendDialog(
    usernameToAdd: String,
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
                    value = usernameToAdd,
                    onValueChange = onUsernameChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Username") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(usernameToAdd) },
                enabled = usernameToAdd.isNotBlank()
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