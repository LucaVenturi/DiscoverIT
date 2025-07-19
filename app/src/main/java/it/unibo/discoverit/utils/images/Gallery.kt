package it.unibo.discoverit.utils.images

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun rememberGalleryLauncher(
    onPicturePicked: (imageUri: Uri) -> Unit = {}
): ImageSourceLauncher {
    var capturedImageUri by remember { mutableStateOf(Uri.EMPTY) }

    val galleryActivityLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null && uri != Uri.EMPTY) {
                capturedImageUri = uri
                onPicturePicked(uri)
            }
        }

    val galleryLauncher = remember(galleryActivityLauncher) {
        object : ImageSourceLauncher {
            override val capturedImageUri get() = capturedImageUri
            override fun captureImage() {
                galleryActivityLauncher.launch("image/*")
            }
        }
    }
    return galleryLauncher
}