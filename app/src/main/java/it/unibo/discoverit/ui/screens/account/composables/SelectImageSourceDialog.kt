package it.unibo.discoverit.ui.screens.account.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
        title = { Text("Seleziona la fonte dell'immagine") },
        text = { Text("Da dove vuoi prendere la foto profilo?") },
        confirmButton = {
            TextButton(onClick = {
                onPickFromGallery()
                galleryLauncher.captureImage()
            }) {
                Text("Dalla Galleria")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onTakePhoto()
                cameraLauncher.captureImage()
            }) {
                Text("Scatta una foto con la Camera")
            }
        }
    )
}