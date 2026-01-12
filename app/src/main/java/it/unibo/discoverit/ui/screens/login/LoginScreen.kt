package it.unibo.discoverit.ui.screens.login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import it.unibo.discoverit.ui.screens.login.composables.LoginContent

/**
 * Composable for the login screen.
 *
 * Displays the login form and handles the login process.
 *
 * @param loginState The state of the login screen.
 * @param loginActions The actions for the login screen.
 */
@Composable
fun LoginScreen(
    loginState: LoginState,
    loginActions: LoginActions,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { innerPadding ->
        LoginContent(
            innerPadding = innerPadding,
            loginState = loginState,
            loginActions = loginActions,
            onNavigateToRegister = onNavigateToRegister
        )
    }

    // When the login is successful, navigate to the home screen.
    LaunchedEffect(loginState.currentPhase) {
        if (loginState.currentPhase == LoginPhase.SUCCESS) {
            onLoginSuccess()
        }
    }
}