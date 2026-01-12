package it.unibo.discoverit.utils.profilepic

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Helper class for managing profile picture storage in the app's private directory.
 *
 * Handles saving, loading, and deletion of profile picture files stored as JPEG.
 * All operations are performed within a dedicated `profilePictures` subdirectory.
 *
 * @property context The application context for accessing files directory.
 */
class ProfilePicStorageHelper(context: Context) {
    /**
     * Directory where profile pictures are stored.
     * Created automatically if it doesn't exist.
     */
    private val directory: File = File(context.filesDir, "profilePictures").apply {
        if (!exists()) {
            mkdirs()
        }
    }

    /**
     * Saves a bitmap as a JPEG file in the profile pictures directory.
     *
     * @param bitmap The bitmap image to save.
     * @param filename The name of the file to create (e.g., "pp_123.jpg").
     * @return The absolute path to the saved file.
     * @throws IOException If the bitmap cannot be compressed or the file cannot be written.
     */
    @Throws(IOException::class)
    fun save(bitmap: Bitmap, filename: String): String {
        val file = File(directory, filename)
        FileOutputStream(file).use { out ->
            if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                throw IOException("Failed to compress and save bitmap")
            }
        }
        return file.absolutePath
    }

    /**
     * Loads a profile picture from the storage directory.
     *
     * @param filename The name of the file to load.
     * @return The decoded [Bitmap], or `null` if the file doesn't exist or cannot be decoded.
     */
    fun load(filename: String): Bitmap? {
        val file = File(directory, filename)
        return if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else {
            null
        }
    }

    /**
     * Deletes a profile picture file from the storage directory.
     *
     * @param filename The name of the file to delete.
     * @return `true` if the file existed and was successfully deleted, `false` otherwise.
     */
    fun delete(filename: String): Boolean {
        val file = File(directory, filename)
        return file.exists() && file.delete()
    }

    /**
     * Gets the absolute file path for a profile picture without loading it.
     *
     * @param filename The name of the file.
     * @return The absolute path to the file, regardless of whether it exists.
     */
    fun getProfilePicturePath(filename: String): String {
        return File(directory, filename).absolutePath
    }
}