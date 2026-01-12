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

/**
 * Composable of a screen that checks the session of the user.
 * It's basically empty and check if there is a user logged in.
 *
 * @param state The state of the screen.
 * @param actions The actions that can be performed on the screen.
 * @param onNavigateToLogin The action to perform when the user is not logged in.
 * @param onNavigateToHome The action to perform when the user is logged in.
 */
@Composable
fun SessionCheckScreen(
    state: SessionCheckState,
    actions: SessionCheckActions,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
) {
    val context = LocalContext.current
    // Activity per il login biometrico.
    val activity = context as FragmentActivity
    // Helper per la gestione della biometria.
    val biometricHelper = remember { BiometricAuthHelper(context) }

    // Get the strings for the biometric login.
    val biometricTitle = stringResource(R.string.biometric_login_title)
    val biometricSubtitle = stringResource(R.string.use_fingerprint)
    val biometricNegativeText = stringResource(R.string.use_password)

    // Depending on the state, navigate to the appropriate screen.
    LaunchedEffect(state.currentPhase) {
        when (state.currentPhase) {
            // User not logged in, navigate to the login screen.
            SessionCheckPhase.USER_NOT_LOGGED_IN -> {
                onNavigateToLogin()
            }
            // User logged in, navigate to the home screen.
            SessionCheckPhase.USER_LOGGED_IN -> {
                onNavigateToHome()
            }
            // User is logged in but biometric authentication is required.
            SessionCheckPhase.BIOMETRIC_REQUIRED -> {
                if (biometricHelper.isBiometricAvailable()) {
                    biometricHelper.authenticate(
                        activity = activity,
                        title = biometricTitle,
                        subtitle = biometricSubtitle,
                        negativeText = biometricNegativeText,
                        onSuccess = {
                            // Biometric authentication succeeded, update the state.
                            actions.onBiometricSuccess()
                        },
                        onError = { msg ->
                            // Biometric authentication failed, navigate to the login screen.
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            onNavigateToLogin()
                        }
                    )
                } else {
                    // Biometric authentication is not available, navigate to the login screen.
                    onNavigateToLogin()
                }
            }
            SessionCheckPhase.CHECKING -> {
                // Waiting...
            }
            SessionCheckPhase.ERROR -> {
                // Error, navigate to the login screen.
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