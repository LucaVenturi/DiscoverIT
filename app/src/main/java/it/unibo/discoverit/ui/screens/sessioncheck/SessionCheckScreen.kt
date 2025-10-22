package it.unibo.discoverit.ui.screens.sessioncheck

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import it.unibo.discoverit.utils.biometric.BiometricAuthHelper

@Composable
fun SessionCheckScreen(
    state: SessionCheckState,
    actions: SessionCheckActions,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
) {
    val context = LocalContext.current
    val activity = context as FragmentActivity
    val biometricHelper = remember { BiometricAuthHelper(context) }

    // Gestione navigazione basata sullo stato
    LaunchedEffect(state.currentPhase) {
        Log.e("SessionCheckScreen", "LaunchedEffect: ${state.currentPhase}")
        when (state.currentPhase) {
            SessionCheckPhase.USER_NOT_LOGGED_IN -> {
                Log.e("SessionCheckScreen", "Navigating to login")
                onNavigateToLogin()
            }
            SessionCheckPhase.USER_LOGGED_IN -> {
                Log.e("SessionCheckScreen", "Navigating to home")
                onNavigateToHome()
            }
            SessionCheckPhase.BIOMETRIC_REQUIRED -> {
                Log.e("SessionCheckScreen", "Starting biometric authentication")
                if (biometricHelper.isBiometricAvailable()) {
                    biometricHelper.authenticate(
                        activity = activity,
                        title = "Biometric login for DiscoverIt",
                        subtitle = "Usa l'impronta digitale per accedere",
                        negativeText = "Usa la password",
                        onSuccess = {
                            Log.e("SessionCheckScreen", "Biometric success")
                            // SOLO l'azione, la navigazione sarà gestita dallo stato
                            actions.onBiometricSuccess()
                        },
                        onError = { msg ->
                            Log.e("SessionCheckScreen", "Biometric error: $msg")
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            // In caso di errore biometrico, manda al login
                            onNavigateToLogin()
                        }
                    )
                } else {
                    Log.e("SessionCheckScreen", "Biometric not available, going to login")
                    // Biometria non disponibile, manda al login
                    onNavigateToLogin()
                }
            }
            SessionCheckPhase.CHECKING -> {
                // Resta in attesa, niente navigazione
                Log.e("SessionCheckScreen", "Still checking session...")
            }
            SessionCheckPhase.ERROR -> {
                Log.e("SessionCheckScreen", "Error occurred, going to login")
                onNavigateToLogin()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { innerPadding ->
        SessionCheckContent(innerPadding, state, actions)
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
            SessionCheckPhase.BIOMETRIC_REQUIRED -> {
                LoadingIndicator("Autenticazione biometrica richiesta...")
            }
            SessionCheckPhase.USER_LOGGED_IN -> {
                LoadingIndicator("Accesso confermato...")
            }
            SessionCheckPhase.USER_NOT_LOGGED_IN -> {
                LoadingIndicator("Reindirizzamento al login...")
            }
            SessionCheckPhase.ERROR -> {
                LoadingIndicator("Errore, reindirizzamento...")
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