package it.unibo.discoverit.utils.profilepic

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ProfilePicStorageHelper(private val context: Context) {
    private val directory: File = File(context.filesDir, "profilePictures").apply {
        if (!exists()) {
            mkdirs()
        }
    }

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

    fun load(filename: String): Bitmap? {
        val file = File(directory, filename)
        return if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else {
            null
        }
    }

    fun delete(filename: String): Boolean {
        val file = File(directory, filename)
        return file.exists() && file.delete()
    }

    fun getProfilePicturePath(filename: String): String {
        return File(directory, filename).absolutePath
    }
}