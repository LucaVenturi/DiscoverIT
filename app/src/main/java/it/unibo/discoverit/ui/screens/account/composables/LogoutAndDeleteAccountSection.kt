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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import it.unibo.discoverit.R

@Composable
fun LogoutAndDeleteAccountSection(
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit
){
    Button(
        onClick = dropUnlessResumed { onLogoutClick() },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.logout))
    }

    Spacer(Modifier.height(8.dp))

    OutlinedButton(
        onClick = dropUnlessResumed { onDeleteAccountClick() } ,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.error
        )
    ) {
        Text(stringResource(R.string.delete_account))
    }
}