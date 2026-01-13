package it.unibo.discoverit.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessStarted
import it.unibo.discoverit.R.string

/**
 * Composable for displaying an empty state UI with a message and an optional refresh button.
 *
 * @param message The message to display in the UI.
 * @param onRefresh The action to perform when the refresh button is clicked.
 */
@Composable
fun EmptyStateUI(message: String, onRefresh: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = dropUnlessStarted {
            onRefresh()
        }) {
            Text(stringResource(string.reload))
        }
    }
}