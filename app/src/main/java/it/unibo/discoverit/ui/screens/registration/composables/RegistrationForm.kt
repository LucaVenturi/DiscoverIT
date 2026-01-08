package it.unibo.discoverit.ui.screens.registration.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.unibo.discoverit.R
import it.unibo.discoverit.ui.composables.PasswordField
import it.unibo.discoverit.ui.composables.UsernameField
import it.unibo.discoverit.ui.screens.registration.RegistrationActions
import it.unibo.discoverit.ui.screens.registration.RegistrationState

@Composable
fun RegistrationForm(
    state: RegistrationState,
    actions: RegistrationActions,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        UsernameField(
            username = state.username,
            onUsernameChanged = actions::onUsernameChanged
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordField(
            password = state.password,
            onPasswordChanged = actions::onPasswordChanged,
            label = stringResource(R.string.password)
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordField(
            password = state.confirmPassword,
            onPasswordChanged = actions::onConfirmPasswordChanged,
            label = stringResource(R.string.confirm_password),
            isError = state.password != state.confirmPassword
        )
    }
}