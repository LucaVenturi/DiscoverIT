package it.unibo.discoverit.ui.screens.sessioncheck

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import it.unibo.discoverit.ui.composables.ErrorMessage

@Composable
fun SessionCheckScreen(
    state: SessionCheckState,
    actions: SessionCheckActions,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    // Gestione navigazione automatica su successo
    LaunchedEffect(state.currentPhase) {
        when(state.currentPhase) {
            SessionCheckPhase.USER_LOGGED_IN ->
                onNavigateToHome()
            SessionCheckPhase.USER_NOT_LOGGED_IN, SessionCheckPhase.ERROR ->
                onNavigateToLogin()
            else -> {}
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { innerPadding ->
        SessionCheckContent(
            innerPadding = innerPadding,
            state = state,
            actions = actions
        )
    }
}

@Composable
private fun SessionCheckContent(
    innerPadding: PaddingValues,
    state: SessionCheckState,
    actions: SessionCheckActions
) {
    Box(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (state.currentPhase) {
            SessionCheckPhase.CHECKING -> {
                LoadingIndicator("Controllo sessione...")
            }
            else -> {
                // Se non sono in checking allora sto navigando verso login o home.
                // Mostra loading durante la navigazione.
                LoadingIndicator("Reindirizzamento...")
            }
        }
    }
}

@Composable
fun LoadingIndicator(msg: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CircularProgressIndicator()
        Text(
            text = msg,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}