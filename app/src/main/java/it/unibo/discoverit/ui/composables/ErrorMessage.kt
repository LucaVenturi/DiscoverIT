package it.unibo.discoverit.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Composable for displaying an error message.
 *
 * @param error The error message to display.
 * @param modifier The modifier to apply to the layout.
 */
@Composable
fun ErrorMessage(
    error: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = error,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier.padding(top = 16.dp)
    )
}