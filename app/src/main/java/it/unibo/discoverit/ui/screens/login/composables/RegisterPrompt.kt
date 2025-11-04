package it.unibo.discoverit.ui.screens.login.composables

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import it.unibo.discoverit.R

@Composable
fun RegisterPrompt(
    onNavigateToRegister: () -> Unit
) {
    TextButton(
        onClick = onNavigateToRegister
    ) {
        Text(stringResource(R.string.register_if_no_account))
    }
}