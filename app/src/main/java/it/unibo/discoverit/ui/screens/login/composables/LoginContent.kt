package it.unibo.discoverit.ui.screens.login.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.unibo.discoverit.ui.screens.login.LoginActions
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
        LoginHeader()

        Spacer(modifier = Modifier.height(24.dp))

        UsernameLoginField(
            username = loginState.username,
            onUsernameChanged = { loginActions.onUsernameChanged(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordLoginField(
            password = loginState.password,
            onPasswordChanged = { loginActions.onPasswordChanged(it) }
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

        RegisterPrompt(onNavigateToRegister)

        loginState.errorMsg?.let { error ->
            LoginErrorMessage(
                error = error
            )
        }
    }
}