package it.unibo.discoverit.ui.screens.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.unibo.discoverit.ui.composables.ErrorMessage
import it.unibo.discoverit.ui.screens.registration.composables.LoginPrompt
import it.unibo.discoverit.ui.screens.registration.composables.RegisterButton
import it.unibo.discoverit.ui.screens.registration.composables.RegistrationForm
import it.unibo.discoverit.ui.screens.registration.composables.RegistrationHeader

@Composable
fun RegistrationScreen(
    state: RegistrationState,
    actions: RegistrationActions,
    onNavigateToLogin: () -> Unit,
    onRegistrationSuccess: () -> Unit
) {
    // Gestione navigazione automatica su successo
    LaunchedEffect(state.currentPhase) {
        if (state.currentPhase == RegistrationPhase.SUCCESS) {
            onRegistrationSuccess()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { innerPadding ->
        RegistrationContent(
            innerPadding = innerPadding,
            state = state,
            actions = actions,
            onNavigateToLogin = onNavigateToLogin
        )
    }
}

@Composable
private fun RegistrationContent(
    innerPadding: PaddingValues,
    state: RegistrationState,
    actions: RegistrationActions,
    onNavigateToLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RegistrationHeader()

        Spacer(modifier = Modifier.height(32.dp))

        RegistrationForm(
            state = state,
            actions = actions
        )

        Spacer(modifier = Modifier.height(32.dp))

        RegisterButton(
            enabled = state.isFormValid && !state.isLoading,
            isLoading = state.isLoading,
            onClick = actions::onRegisterClicked
        )

        // Mostra errore se presente
        state.error?.let { error ->
            ErrorMessage(error = error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LoginPrompt(onNavigateToLogin = onNavigateToLogin)
    }
}