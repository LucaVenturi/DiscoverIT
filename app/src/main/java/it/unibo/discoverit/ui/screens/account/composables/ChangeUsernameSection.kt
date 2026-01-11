package it.unibo.discoverit.ui.screens.account.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import it.unibo.discoverit.R.string
import it.unibo.discoverit.R.string.save_changes

@Composable
fun ChangeUsernameSection(
    shownUsername: String,
    onUsernameChange: (String) -> Unit,
    isUsernameChanged: Boolean,
    onSaveClick: () -> Unit,
) {
    // Cambia nome utente
    Text(
        text = stringResource(string.username),
        style = MaterialTheme.typography.titleMedium
    )

    Spacer(Modifier.height(4.dp))

    Row {
        OutlinedTextField(
            value = shownUsername,
            onValueChange = onUsernameChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            trailingIcon = {
                SaveUsernameChangesIconButton(
                    usernameChanged = isUsernameChanged,
                    onClick = onSaveClick
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
        IconButton(onClick = dropUnlessResumed { onClick() }) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(save_changes),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}