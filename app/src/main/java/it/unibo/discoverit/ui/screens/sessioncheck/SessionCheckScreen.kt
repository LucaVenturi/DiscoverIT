package it.unibo.discoverit.ui.screens.sessioncheck

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


    LaunchedEffect(state.currentPhase) {
        when (state.currentPhase) {
            SessionCheckPhase.BIOMETRIC_REQUIRED -> {
                if (biometricHelper.isBiometricAvailable()) {
                    biometricHelper.authenticate(
                        activity = activity,
                        title = "Biometric login for DiscoverIt",
                        subtitle = "Usa l’impronta digitale per accedere",
                        negativeText = "Usa la password",
                        onSuccess = { actions.onBiometricSuccess(); onNavigateToHome() },
                        onError = { msg ->
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                    )
                } else {
                    onNavigateToLogin()
                }
            }
            else -> {}
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