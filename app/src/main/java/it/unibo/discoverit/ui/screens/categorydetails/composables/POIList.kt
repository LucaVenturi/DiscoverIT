package it.unibo.discoverit.ui.screens.categorydetails.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import it.unibo.discoverit.data.database.entities.PointOfInterest

@Composable
fun POIList(
    poiList: List<PointOfInterest>,
    onPOIClick: (Long) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        items(poiList.size) { index ->
            POICard(
                poi = poiList[index],
                onPOIClick = onPOIClick
            )
        }
    }
}
