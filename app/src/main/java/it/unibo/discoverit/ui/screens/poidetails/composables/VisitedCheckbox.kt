package it.unibo.discoverit.ui.screens.poidetails.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun VisitedCheckbox(
    isVisited: Boolean,
    onToggleVisit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text("Visitato")
        Checkbox(
            checked = isVisited,
            onCheckedChange = { onToggleVisit() }
        )
    }
}