package it.unibo.discoverit.ui.screens.login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import it.unibo.discoverit.ui.screens.login.composables.LoadingScreen
import it.unibo.discoverit.ui.screens.login.composables.LoginContent

@Composable
fun LoginScreen(
    loginState: LoginState,
    loginActions: LoginActions,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    when (loginState.currentPhase) {
        LoginPhase.CHECKING_SESSION -> {
            LoadingScreen()
        }
        LoginPhase.SUCCESS -> {
            // Non mostra nulla, LaunchedEffect gestirà la navigazione
        }
        else -> {
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
        }
    }

    // Naviga alla Home dopo il login riuscito
    LaunchedEffect(loginState.currentPhase) {
        if (loginState.currentPhase == LoginPhase.SUCCESS) {
            onLoginSuccess()
        }
    }
}