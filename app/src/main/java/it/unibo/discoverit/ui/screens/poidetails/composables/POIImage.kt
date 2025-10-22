package it.unibo.discoverit.ui.screens.poidetails.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun POIImage(
    imagePath: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imagePath?.let { "file:///android_asset/$it" })
            .crossfade(true)
            .build(),
        contentDescription = "POI Image",
        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
        placeholder = rememberVectorPainter(Icons.Default.Place),
        error = rememberVectorPainter(Icons.Default.Place),
        fallback = rememberVectorPainter(Icons.Default.Place),
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.5f) // circa 3:2
            .clip(RoundedCornerShape(16.dp))
    )
}