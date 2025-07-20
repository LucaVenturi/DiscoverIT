package it.unibo.discoverit.ui.screens.account.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LogoutAndDeleteAccountSection(
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit
){
    Button(
        onClick = onLogoutClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Logout")
    }

    Spacer(Modifier.height(8.dp))

    OutlinedButton(
        onClick = onDeleteAccountClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.error
        )
    ) {
        Text("Elimina account")
    }
}