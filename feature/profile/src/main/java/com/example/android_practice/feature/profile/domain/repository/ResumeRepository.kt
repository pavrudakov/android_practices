package com.example.android_practice.feature.profile.domain.repository

import com.example.android_practice.feature.profile.domain.model.DownloadedFile
interface ResumeRepository {
    suspend fun download(url: String): DownloadedFile
}
