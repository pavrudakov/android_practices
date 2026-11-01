package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.error.InvalidUrlException
import com.example.android_practice.domain.model.DownloadedFile
import com.example.android_practice.domain.repository.ResumeRepository
import javax.inject.Inject

class DownloadResumeUseCase @Inject constructor(
    private val resumeRepository: ResumeRepository
) {
    suspend operator fun invoke(rawUrl: String): DownloadedFile {
        if (rawUrl.trim().isBlank()) {
            throw InvalidUrlException("Ссылка на резюме не указана")
        }
        val normalizedUrl = SaveProfileUseCase.normalizeAndValidateUrl(rawUrl)
        return resumeRepository.download(normalizedUrl)
    }
}
