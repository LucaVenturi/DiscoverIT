package it.unibo.discoverit.ui.screens.account.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.dropUnlessResumed
import it.unibo.discoverit.R
import it.unibo.discoverit.utils.images.ImageSourceLauncher
import it.unibo.discoverit.utils.images.rememberCameraLauncher
import it.unibo.discoverit.utils.images.rememberGalleryLauncher

@Composable
fun SelectImageSourceDialog(
    onPickFromGallery: () -> Unit,
    onTakePhoto: () -> Unit,
    onDismiss: () -> Unit,
    galleryLauncher: ImageSourceLauncher = rememberGalleryLauncher(),
    cameraLauncher: ImageSourceLauncher = rememberCameraLauncher()
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.select_profile_pic)) },
        text = { Text(stringResource(R.string.select_profile_pic_source)) },
        confirmButton = {
            TextButton(onClick = dropUnlessResumed {
                onPickFromGallery()
                galleryLauncher.captureImage()
            }) {
                Text(stringResource(R.string.from_gallery))
            }
        },
        dismissButton = {
            TextButton(onClick = dropUnlessResumed {
                onTakePhoto()
                cameraLauncher.captureImage()
            }) {
                Text(stringResource(R.string.from_camera))
            }
        }
    )
}