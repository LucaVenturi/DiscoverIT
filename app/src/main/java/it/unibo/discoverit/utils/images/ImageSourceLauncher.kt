package it.unibo.discoverit.utils.images

import android.net.Uri

/**
 * Interface for launching image capture from different sources (camera or gallery).
 */
interface ImageSourceLauncher {
    /**
     * The URI of the last captured or selected image.
     */
    val capturedImageUri: Uri

    /**
     * Launches the image capture flow (camera or gallery picker).
     */
    fun captureImage()
}