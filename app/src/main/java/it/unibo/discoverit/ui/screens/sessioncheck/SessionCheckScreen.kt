package it.unibo.discoverit.ui.screens.sessioncheck

import android.util.Log
import androidx.biometric.BiometricManager
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

@Composable
fun SessionCheckScreen(
    state: SessionCheckState,
    actions: SessionCheckActions,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    activity: FragmentActivity? // Aggiungi questo parametro
) {
    val context = LocalContext.current
    val biometricManager = BiometricManager.from(context)

    LaunchedEffect(state.currentPhase) {
        when (state.currentPhase) {
            SessionCheckPhase.USER_LOGGED_IN -> onNavigateToHome()
            SessionCheckPhase.USER_NOT_LOGGED_IN, SessionCheckPhase.ERROR -> onNavigateToLogin()
            SessionCheckPhase.BIOMETRIC_REQUIRED -> {
                Log.d("SessionCheckScreen", "Biometric authentication required")
                if (activity != null && biometricManager.canAuthenticate(STRONG) {
                    Log.d("SessionCheckScreen", "Biometric authentication available")
                    biometricAdapter.authenticate(
                        activity, // Ora abbiamo l'activity garantita
                        onSuccess = {
                            actions.onBiometricSuccess()
                            onNavigateToHome()
                        },
                        onFailure = {
                            Log.e("SessionCheckScreen", "Biometric authentication failed: $it")
                            onNavigateToLogin()
                        }
                    )
                } else {
                    Log.d("SessionCheckScreen", "Biometric authentication not available")
                    onNavigateToLogin() // fallback
                }
            }
            else -> {}
        }
    }

    // Resto del codice invariato...
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