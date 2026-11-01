package com.example.android_practice.data.repository

import android.content.Context
import android.os.Environment
import androidx.core.content.FileProvider
import com.example.android_practice.domain.model.DownloadedFile
import com.example.android_practice.domain.repository.ResumeRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class ResumeRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ResumeRepository {

    override suspend fun download(url: String): DownloadedFile = withContext(Dispatchers.IO) {
        val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            ?: File(context.filesDir, "resumes").apply { if (!exists()) mkdirs() }

        val stubFile = File(downloadsDir, "Rudakov_Pavel_Resume.pdf")

        val pdfContent = """
            %PDF-1.4
            1 0 obj
            << /Type /Catalog /Pages 2 0 R >>
            endobj
            2 0 obj
            << /Type /Pages /Kids [3 0 R] /Count 1 >>
            endobj
            3 0 obj
            << /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Contents 4 0 R >>
            endobj
            4 0 obj
            << /Length 50 >>
            stream
            BT
            /F1 16 Tf
            50 720 Td
            (Rudakov Pavel Resume) Tj
            ET
            endstream
            xref
            0 5
            0000000000 65535 f 
            0000000009 00000 n 
            0000000058 00000 n 
            0000000115 00000 n 
            0000000212 00000 n 
            trailer
            << /Size 5 /Root 1 0 R >>
            startxref
            287
            %%EOF
        """.trimIndent().toByteArray(Charsets.UTF_8)

        stubFile.writeBytes(pdfContent)

        val authority = "${context.packageName}.fileprovider"
        val contentUri = FileProvider.getUriForFile(context, authority, stubFile)

        DownloadedFile(
            uri = contentUri.toString(),
            mimeType = "application/pdf",
            fileName = "Rudakov_Pavel_Resume.pdf"
        )
    }
}
