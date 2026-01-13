package it.unibo.discoverit.ui.screens.account.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessStarted
import coil.compose.AsyncImage
import coil.request.ImageRequest
import it.unibo.discoverit.R

/**
 * Composable for displaying the profile picture section.
 *
 * @param profilePicPath The path to the profile picture.
 * @param onChangeProfilePicClick The action to perform when the user clicks on the
 * change profile picture button.
 */
@Composable
fun ProfilePicSection(
    profilePicPath: String?,
    onChangeProfilePicClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center
    ) {
        // Show the profile picture using the Coil library
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(profilePicPath)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.profile_pic),
            contentScale = ContentScale.Crop,
            placeholder = rememberVectorPainter(Icons.Default.AccountCircle),
            error = rememberVectorPainter(Icons.Default.AccountCircle),
            fallback = rememberVectorPainter(Icons.Default.AccountCircle),
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape),
        )
    }

    Spacer(Modifier.height(8.dp))

    TextButton(
        onClick = dropUnlessStarted { onChangeProfilePicClick() }
    ) {
        Text(stringResource(R.string.change_profile_pic))
    }
}