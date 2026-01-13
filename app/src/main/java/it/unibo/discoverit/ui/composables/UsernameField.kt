package it.unibo.discoverit.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import it.unibo.discoverit.R.string

/**
 * Composable for displaying a username field.
 *
 * @param username The current username value.
 * @param onUsernameChanged The callback to be invoked when the username changes.
 * @param modifier The modifier to be applied to the username field.
 * @param isError Whether the username field is in an error state.
 */
@Composable
fun UsernameField(
    username: String,
    onUsernameChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = username,
        onValueChange = onUsernameChanged,
        label = { Text(stringResource(string.username)) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        isError = isError
    )
}