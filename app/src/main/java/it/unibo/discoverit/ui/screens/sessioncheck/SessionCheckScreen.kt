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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import it.unibo.discoverit.R
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
        when (state.currentPhase) {
            SessionCheckPhase.USER_NOT_LOGGED_IN -> {
                onNavigateToLogin()
            }
            SessionCheckPhase.USER_LOGGED_IN -> {
                onNavigateToHome()
            }
            SessionCheckPhase.BIOMETRIC_REQUIRED -> {
                if (biometricHelper.isBiometricAvailable()) {
                    biometricHelper.authenticate(
                        activity = activity,
                        title = context.getString(R.string.biometric_login_title),
                        subtitle = context.getString(R.string.use_fingerprint),
                        negativeText = context.getString(R.string.use_password),
                        onSuccess = {
                            // SOLO l'azione, la navigazione sarà gestita dallo stato
                            actions.onBiometricSuccess()
                        },
                        onError = { msg ->
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            // In caso di errore biometrico, manda al login
                            onNavigateToLogin()
                        }
                    )
                } else {
                    // Biometria non disponibile, manda al login
                    onNavigateToLogin()
                }
            }
            SessionCheckPhase.CHECKING -> {
                // Resta in attesa, niente navigazione
            }
            SessionCheckPhase.ERROR -> {
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
                LoadingIndicator(stringResource(R.string.check_session))
            }
            SessionCheckPhase.BIOMETRIC_REQUIRED -> {
                LoadingIndicator(stringResource(R.string.biometric_login_required))
            }
            SessionCheckPhase.USER_LOGGED_IN -> {
                LoadingIndicator(stringResource(R.string.login_successful))
            }
            SessionCheckPhase.USER_NOT_LOGGED_IN -> {
                LoadingIndicator(stringResource(R.string.redirect_to_login))
            }
            SessionCheckPhase.ERROR -> {
                LoadingIndicator(stringResource(R.string.error_redirecting))
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