package it.unibo.discoverit.ui.screens.account.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.dropUnlessStarted
import it.unibo.discoverit.R
import it.unibo.discoverit.utils.images.ImageSourceLauncher
import it.unibo.discoverit.utils.images.rememberCameraLauncher
import it.unibo.discoverit.utils.images.rememberGalleryLauncher

/**
 * Custom Dialog to select the source of the profile picture.
 *
 * @param onPickFromGallery The action to perform when the user picks an image from the gallery.
 * @param onTakePhoto The action to perform when the user takes a photo with the camera.
 * @param onDismiss The action to perform when the dialog is dismissed.
 * @param galleryLauncher The launcher for picking an image from the gallery.
 * @param cameraLauncher The launcher for taking a photo with the camera.
 */
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
            TextButton(onClick = dropUnlessStarted {
                onPickFromGallery()
                galleryLauncher.captureImage()
            }) {
                Text(stringResource(R.string.from_gallery))
            }
        },
        dismissButton = {
            TextButton(onClick = dropUnlessStarted {
                onTakePhoto()
                cameraLauncher.captureImage()
            }) {
                Text(stringResource(R.string.from_camera))
            }
        }
    )
}