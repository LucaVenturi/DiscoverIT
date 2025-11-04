package it.unibo.discoverit.ui.screens.login.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import it.unibo.discoverit.R

@Composable
fun LoginHeader() {
    Text(
        text = stringResource(R.string.login_into_discoverit),
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.primary
    )
}