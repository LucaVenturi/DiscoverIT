package it.unibo.discoverit.ui.screens.poidetails.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import it.unibo.discoverit.R

@Composable
fun OutOfRangeMessage(
    show: Boolean,
    distance: Float?,
    modifier: Modifier = Modifier
) {
    if (show && distance != null) {
        Text(
            text = stringResource(R.string.too_far_from_x_poi, distance.toInt()),
            modifier = modifier.padding(top = 8.dp),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}