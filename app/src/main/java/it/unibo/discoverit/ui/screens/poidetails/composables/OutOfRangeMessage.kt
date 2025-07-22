package it.unibo.discoverit.ui.screens.poidetails.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun OutOfRangeMessage(
    show: Boolean,
    distance: Float?,
    modifier: Modifier = Modifier
) {
    if (show && distance != null) {
        Text(
            text = "Sei troppo lontano da questo punto di interesse: ${distance.toInt()} m",
            modifier = modifier.padding(top = 8.dp),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}