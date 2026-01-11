package it.unibo.discoverit.ui.screens.registration.composables

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.dropUnlessResumed
import it.unibo.discoverit.R

@Composable
fun LoginPrompt(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = dropUnlessResumed { onNavigateToLogin() },
        modifier = modifier
    ) {
        Text(stringResource(R.string.login_if_has_account))
    }
}