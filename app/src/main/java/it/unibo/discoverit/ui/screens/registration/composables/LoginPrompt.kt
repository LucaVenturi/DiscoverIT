package it.unibo.discoverit.ui.screens.registration.composables

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LoginPrompt(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onNavigateToLogin,
        modifier = modifier
    ) {
        Text("Hai già un account? Accedi")
    }
}