package it.unibo.discoverit.ui.screens.login.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.unibo.discoverit.R
import it.unibo.discoverit.ui.composables.ErrorMessage
import it.unibo.discoverit.ui.composables.PasswordField
import it.unibo.discoverit.ui.composables.UsernameField
import it.unibo.discoverit.ui.screens.login.LoginActions
import it.unibo.discoverit.ui.screens.login.LoginError
import it.unibo.discoverit.ui.screens.login.LoginPhase
import it.unibo.discoverit.ui.screens.login.LoginState


@Composable
fun LoginContent(
    innerPadding: PaddingValues,
    loginState: LoginState,
    loginActions: LoginActions,
    onNavigateToRegister: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val isError = loginState.error != null

        // Login Header.
        Text(
            text = stringResource(R.string.login_into_discoverit),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        UsernameField(
            username = loginState.username,
            onUsernameChanged = { loginActions.onUsernameChanged(it) },
            isError = isError
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordField(
            password = loginState.password,
            onPasswordChanged = { loginActions.onPasswordChanged(it) },
            label = stringResource(R.string.password),
            isError = isError
        )

        Spacer(modifier = Modifier.height(32.dp))

        LoginButton(
            enabled = loginState.username.isNotBlank() &&
                    loginState.password.isNotBlank() &&
                    loginState.currentPhase == LoginPhase.IDLE,
            isLoading = loginState.currentPhase == LoginPhase.LOADING,
            onClick = { loginActions.onLoginClicked() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // If the user does not have an account, can navigate to the register screen.
        RegisterPrompt(onNavigateToRegister)

        // In case of error, show the error message.
        loginState.error?.let { error ->
            ErrorMessage(
                error = when (error) {
                    LoginError.InvalidCredentials -> stringResource(R.string.invalid_credentials)
                    LoginError.UserNotFound -> stringResource(R.string.user_not_found)
                    is LoginError.Other -> error.errMsg
                }
            )
        }
    }
}