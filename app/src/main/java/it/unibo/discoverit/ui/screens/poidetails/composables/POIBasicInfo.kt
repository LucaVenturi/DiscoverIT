package it.unibo.discoverit.ui.screens.poidetails.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.unibo.discoverit.data.database.entities.PointOfInterest

@Composable
fun POIBasicInfo(
    poi: PointOfInterest,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Titolo
        Text(
            text = poi.name,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(8.dp))

        // Indirizzo
        Text(
            text = buildString {
                append(poi.address.street)
                poi.address.civicNumber?.let { append(" $it") }
                append(" ${poi.address.province}")
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}