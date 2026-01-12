package it.unibo.discoverit.utils.images

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File

/**
 * Creates and remembers a camera launcher for capturing photos.
 *
 * @param onPictureTaken Callback invoked when a picture is successfully taken,
 * receiving the URI of the captured image.
 * @return An [ImageSourceLauncher] that can be used to launch the camera.
 */
@Composable
fun rememberCameraLauncher(
    onPictureTaken: (imageUri: Uri) -> Unit = {}
): ImageSourceLauncher {
    val ctx = LocalContext.current

    var imageUri by remember { mutableStateOf(Uri.EMPTY) }
    var capturedImageUri by remember { mutableStateOf(Uri.EMPTY) }

    val cameraActivityLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { pictureTaken ->
            if (!pictureTaken) return@rememberLauncherForActivityResult
            capturedImageUri = imageUri
            onPictureTaken(capturedImageUri)
        }

    val cameraLauncher = remember(cameraActivityLauncher) {
        object : ImageSourceLauncher {
            override val capturedImageUri get() = capturedImageUri
            override fun captureImage() {
                val imageFile = File.createTempFile(
                    "tmp_image",
                    ".jpg",
                    ctx.cacheDir
                )
                imageUri = FileProvider.getUriForFile(
                    ctx,
                    ctx.packageName + ".provider",
                    imageFile
                )
                cameraActivityLauncher.launch(imageUri)
            }
        }
    }
    return cameraLauncher
}