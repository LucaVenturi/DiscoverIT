package it.unibo.discoverit.ui.screens.categorydetails.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessStarted
import coil.compose.AsyncImage
import coil.request.ImageRequest
import it.unibo.discoverit.data.database.entities.PointOfInterest

@Composable
fun POICard(
    poi: PointOfInterest,
    modifier: Modifier = Modifier,
    onPOIClick: (Long) -> Unit
) {
    val context = LocalContext.current

    ElevatedCard(
        modifier = modifier
            .aspectRatio(0.85f) // Slightly wider than tall.
            .padding(4.dp)
            .clickable(onClick = dropUnlessStarted { onPOIClick(poi.poiId) })
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Image: 70% of the space.
            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // Use Coil to load the image from the assets folder.
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("file:///android_asset/${poi.imagePath}")
                        .crossfade(true)
                        .build(),
                    contentDescription = poi.name,
                    contentScale = ContentScale.Crop,
                    placeholder = rememberVectorPainter(Icons.Default.Image),
                    error = rememberVectorPainter(Icons.Default.Image),
                    fallback = rememberVectorPainter(Icons.Default.Image),
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Text: 30% of the space.
            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = poi.name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}