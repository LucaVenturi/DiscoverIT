package it.unibo.discoverit.ui.screens.poidetails.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.unibo.discoverit.data.database.entities.PointOfInterest

@Composable
fun POIDetailsContent(
    poi: PointOfInterest,
    isVisited: Boolean,
    showOutOfRangeMessage: Boolean,
    distanceToPOI: Float?,
    onToggleVisit: () -> Unit,
    onOpenInMap: () -> Unit,
    onUseGPS: () -> Unit,
    modifier: Modifier = Modifier,
    isButtonLoading: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        POIImagePlaceholder()

        Spacer(Modifier.height(16.dp))

        POIBasicInfo(poi = poi)

        Spacer(Modifier.height(16.dp))

        POIDescription(description = poi.description)

        Spacer(Modifier.height(24.dp))

        POIActionButtons(
            isVisited = isVisited,
            onToggleVisit = onToggleVisit,
            onOpenMap = onOpenInMap
        )

        Spacer(Modifier.height(16.dp))

        GPSButton(
            onClick = onUseGPS,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            isLoading = isButtonLoading
        )

        OutOfRangeMessage(
            show = showOutOfRangeMessage,
            distance = distanceToPOI,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}