package it.unibo.discoverit.ui.screens.login.composables

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun RegisterPrompt(
    onNavigateToRegister: () -> Unit
) {
    TextButton(
        onClick = onNavigateToRegister
    ) {
        Text("Non hai un account? Registrati")
    }
}