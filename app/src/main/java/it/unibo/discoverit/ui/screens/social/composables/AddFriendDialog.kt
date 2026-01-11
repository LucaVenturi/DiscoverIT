package it.unibo.discoverit.ui.screens.social.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import it.unibo.discoverit.R

@Composable
fun AddFriendDialog(
    usernameToAdd: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    onUsernameChange: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_friend)) },
        text = {
            Column {
                Text(stringResource(R.string.insert_friends_username))
                OutlinedTextField(
                    value = usernameToAdd,
                    onValueChange = onUsernameChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(stringResource(R.string.username)) }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(usernameToAdd) },
                enabled = usernameToAdd.isNotBlank()
            ) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.dismiss))
            }
        }
    )
}