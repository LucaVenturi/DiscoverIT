package it.unibo.discoverit.ui.screens.registration.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RegistrationHeader(
    modifier: Modifier = Modifier
) {
    Text(
        text = "Register",
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}