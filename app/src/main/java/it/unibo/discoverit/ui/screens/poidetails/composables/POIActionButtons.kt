package it.unibo.discoverit.ui.screens.poidetails.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun POIActionButtons(
    isVisited: Boolean,
    onToggleVisit: () -> Unit,
    onOpenMap: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MapButton(onClick = onOpenMap)

        VisitedCheckbox(
            isVisited = isVisited,
            onToggleVisit = onToggleVisit
        )
    }
}