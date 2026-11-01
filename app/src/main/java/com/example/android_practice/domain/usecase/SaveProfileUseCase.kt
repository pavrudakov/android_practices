package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.error.InvalidUrlException
import com.example.android_practice.domain.model.UserProfile
import com.example.android_practice.domain.repository.ProfileRepository
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import javax.inject.Inject

class SaveProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(profile: UserProfile) {
        val trimmedName = profile.fullName.trim().take(100)
        val trimmedPosition = profile.position.trim().take(100)
        val normalizedUrl = normalizeAndValidateUrl(profile.resumeUrl)

        val updatedProfile = profile.copy(
            fullName = trimmedName,
            position = trimmedPosition,
            resumeUrl = normalizedUrl
        )

        profileRepository.save(updatedProfile)
    }

    companion object {
        fun normalizeAndValidateUrl(rawUrl: String): String {
            val trimmed = rawUrl.trim()
            if (trimmed.isBlank()) {
                return ""
            }

            val lower = trimmed.lowercase()
            val withScheme = when {
                lower.startsWith("https://") -> trimmed
                lower.startsWith("http://") -> "https://" + trimmed.substring(7)
                lower.contains("://") -> throw InvalidUrlException("Допускаются только ссылки http/https")
                else -> "https://$trimmed"
            }

            val encodedSpaces = withScheme.replace(" ", "%20")
            val httpUrl = encodedSpaces.toHttpUrlOrNull()
                ?: throw InvalidUrlException("Некорректный формат ссылки")

            val host = httpUrl.host
            if (host.isBlank() || !host.contains('.') || host.startsWith('.') || host.endsWith('.')) {
                throw InvalidUrlException("Некорректный домен в ссылке")
            }

            return httpUrl.toString()
        }
    }
}
