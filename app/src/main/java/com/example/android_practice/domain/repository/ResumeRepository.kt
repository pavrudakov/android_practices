package com.example.android_practice.domain.repository

import com.example.android_practice.domain.model.DownloadedFile

interface ResumeRepository {
    suspend fun download(url: String): DownloadedFile
}
