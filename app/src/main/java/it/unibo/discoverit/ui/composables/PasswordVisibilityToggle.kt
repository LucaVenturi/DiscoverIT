package it.unibo.discoverit.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.dropUnlessStarted
import it.unibo.discoverit.R
import it.unibo.discoverit.R.string


/**
 * Composable for displaying a password visibility toggle button.
 *
 * @param isVisible Whether the password is currently visible.
 * @param onToggle The callback to be invoked when the visibility toggle is clicked.
 * @param modifier The modifier to be applied to the toggle button.
 */
@Composable
fun PasswordVisibilityToggle(
    isVisible: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = dropUnlessStarted { onToggle() } ,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isVisible) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            },
            contentDescription = if (isVisible) {
                stringResource(R.string.hide_password)
            } else {
                stringResource(R.string.show_password)
            }
        )
    }
}