package com.example.android_practice.data.repository

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.example.android_practice.domain.model.CameraTarget
import com.example.android_practice.domain.repository.AvatarRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class AvatarRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AvatarRepository {

    private val avatarsDir: File
        get() = File(context.filesDir, "avatars").apply { if (!exists()) mkdirs() }

    override suspend fun importFromUri(sourceUri: String): String = withContext(Dispatchers.IO) {
        val parsedUri = Uri.parse(sourceUri)
        val mimeType = context.contentResolver.getType(parsedUri)
        val extension = if (mimeType != null) {
            MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: "jpg"
        } else {
            "jpg"
        }

        val targetFile = File(avatarsDir, "avatar_${System.currentTimeMillis()}.$extension")

        context.contentResolver.openInputStream(parsedUri)?.use { input ->
            targetFile.outputStream().use { output ->
                input.copyTo(output)
            }
        } ?: throw IllegalArgumentException("Failed to open stream for URI: $sourceUri")

        Uri.fromFile(targetFile).toString()
    }

    override suspend fun createCameraTarget(): CameraTarget = withContext(Dispatchers.IO) {
        val targetFile = File(avatarsDir, "avatar_cam_${System.currentTimeMillis()}.jpg")
        val authority = "${context.packageName}.fileprovider"
        val captureUri = FileProvider.getUriForFile(context, authority, targetFile)

        CameraTarget(
            captureUri = captureUri.toString(),
            storedUri = Uri.fromFile(targetFile).toString()
        )
    }

    override suspend fun delete(uri: String): Unit = withContext(Dispatchers.IO) {
        try {
            val parsed = Uri.parse(uri)
            val filePath = parsed.path ?: uri
            val file = File(filePath)
            if (file.exists() && file.canonicalPath.startsWith(avatarsDir.canonicalPath)) {
                file.delete()
            }
        } catch (e: Exception) {
            // best effort
        }
    }
}
