package it.unibo.discoverit.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Composable for displaying a password field with visibility toggle.
 *
 * @param password The current password value.
 * @param onPasswordChanged The callback to be invoked when the password changes.
 * @param label The label for the password field.
 * @param modifier The modifier to be applied to the password field.
 * @param isError Whether the password field is in an error state.
 */
@Composable
fun PasswordField(
    password: String,
    onPasswordChanged: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChanged,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isError = isError,
        trailingIcon = {
            PasswordVisibilityToggle(
                isVisible = passwordVisible,
                onToggle = { passwordVisible = !passwordVisible }
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            errorTrailingIconColor = MaterialTheme.colorScheme.error,
            focusedTrailingIconColor = MaterialTheme.colorScheme.primary
        )
    )
}