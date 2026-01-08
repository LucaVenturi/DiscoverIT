package it.unibo.discoverit.ui.screens.poidetails.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.unibo.discoverit.R
import it.unibo.discoverit.ui.composables.LoadingButton

@Composable
fun GPSButton(
    onClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    LoadingButton(
        isLoading = isLoading,
        onClick = onClick,
        modifier = modifier,
        loadingIndicatorSize = 16.dp
    ) {
        Icon(
            imageVector = Icons.Default.GpsFixed,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(stringResource(R.string.check_position))
    }
}