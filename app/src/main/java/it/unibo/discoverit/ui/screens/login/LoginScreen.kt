package it.unibo.discoverit.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.unibo.discoverit.Destination

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

    // Naviga alla Home dopo il login riuscito
    LaunchedEffect(loginState.currentPhase) {
        if (loginState.currentPhase == LoginPhase.SUCCESS) {
            // Aggiungi un piccolo delay per garantire che lo stato sia propagato
            kotlinx.coroutines.delay(100) // Opzionale, ma utile in alcuni casi
            onLoginSuccess()
        }
    }
}

@Composable
private fun LoginContent(
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

@Composable
private fun LoginHeader() {
    Text(
        text = "Accedi a DiscoverIt",
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun UsernameLoginField(
    username: String,
    onUsernameChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = username,
        onValueChange = onUsernameChanged,
        label = { Text("Username") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}

@Composable
private fun PasswordLoginField(
    password: String,
    onPasswordChanged: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChanged,
        label = { Text("Password") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            PasswordVisibilityToggle(
                isVisible = passwordVisible,
                onToggle = { passwordVisible = !passwordVisible }
            )
        }
    )
}

@Composable
private fun LoginButton(
    enabled: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = enabled
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text("Accedi", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun RegisterPrompt(
    onNavigateToRegister: () -> Unit
) {
    TextButton(
        onClick = onNavigateToRegister
    ) {
        Text("Non hai un account? Registrati")
    }
}

@Composable
private fun LoginErrorMessage(
    error: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun PasswordVisibilityToggle(
    isVisible: Boolean,
    onToggle: () -> Unit
) {
    IconButton(onClick = onToggle) {
        Icon(
            imageVector = if (isVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
            contentDescription = "Toggle password visibility"
        )
    }
}