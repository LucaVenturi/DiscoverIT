package it.unibo.discoverit.utils.images

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

/**
 * Converts an image URI to a Bitmap.
 * Uses the appropriate API based on the Android version.
 *
 * @param imageUri The URI of the image to convert.
 * @param contentResolver The content resolver to access the image.
 * @return The decoded [Bitmap].
 */
fun uriToBitmap(imageUri: Uri, contentResolver: ContentResolver): Bitmap {
    val bitmap = when {
        Build.VERSION.SDK_INT < 28 -> {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
        }
        else -> {
            val source = ImageDecoder.createSource(contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        }
    }
    return bitmap
}