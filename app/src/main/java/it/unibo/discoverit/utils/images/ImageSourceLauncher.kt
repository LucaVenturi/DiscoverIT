package it.unibo.discoverit.utils.images

import android.net.Uri

interface ImageSourceLauncher {
    val capturedImageUri: Uri
    fun captureImage()
}