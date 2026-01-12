package it.unibo.discoverit.ui.screens.categorydetails.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.unibo.discoverit.data.database.entities.PointOfInterest

@Composable
fun POIList(
    poiList: List<PointOfInterest>,
    onPOIClick: (Long) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp), // Adaptive number of columns for different screen sizes
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp) // Extra space around the grid
    ) {
        items(poiList.size) { index ->
            POICard(
                poi = poiList[index],
                onPOIClick = onPOIClick
            )
        }
    }
}