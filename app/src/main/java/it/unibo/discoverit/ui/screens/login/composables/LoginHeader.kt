package it.unibo.discoverit.ui.screens.login.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun LoginHeader() {
    Text(
        text = "Accedi a DiscoverIt",
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.primary
    )
}