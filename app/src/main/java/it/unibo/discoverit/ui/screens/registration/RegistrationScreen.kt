package it.unibo.discoverit.ui.screens.registration

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
import it.unibo.discoverit.ui.composables.MyTopAppBar

@Composable
fun RegistrationScreen(
    navController: NavHostController,
    state: RegistrationState,
    actions: RegistrationActions,
    onRegistrationSuccess: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { innerPadding ->
        RegistrationContent(
            innerPadding = innerPadding,
            state = state,
            actions = actions
        )
    }
}

@Composable
private fun RegistrationContent(
    innerPadding: PaddingValues,
    state: RegistrationState,
    actions: RegistrationActions
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

        Spacer(modifier = Modifier.height(24.dp))

        UsernameField(
            username = state.username,
            onUsernameChanged = {actions.onUsernameChanged(it)}
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordField(
            password = state.password,
            onPasswordChanged = {actions.onPasswordChanged(it)},
            label = "Password"
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordField(
            password = state.confirmPassword,
            onPasswordChanged = {actions.onConfirmPasswordChanged(it)},
            label = "Conferma Password"
        )

        Spacer(modifier = Modifier.height(32.dp))

        RegisterButton(
            enabled = state.username.isNotBlank() &&
                    state.password.isNotBlank() &&
                    state.confirmPassword.isNotBlank() &&
                    !state.isLoading,
            isLoading = state.isLoading,
            onClick = { actions.onRegisterClicked() }
        )

        state.error?.let { error ->
            ErrorMessage(
                error = error,
                onDismiss = { actions.onDismissError() }
            )
        }
    }
}

@Composable
private fun RegistrationHeader() {
    Text(
        text = "Registrati",
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun UsernameField(
    username: String,
    onUsernameChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = username,
        onValueChange = onUsernameChanged,
        label = { Text("Username") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
private fun PasswordField(
    password: String,
    onPasswordChanged: (String) -> Unit,
    label: String
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChanged,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
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

@Composable
private fun RegisterButton(
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
            Text("Registrati", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun ErrorMessage(
    error: String,
    onDismiss: () -> Unit
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
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onDismiss) {
            Text("Chiudi")
        }
    }
}