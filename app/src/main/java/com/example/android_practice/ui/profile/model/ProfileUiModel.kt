package com.example.android_practice.ui.profile.model

import com.example.android_practice.domain.model.UserProfile

data class ProfileUiModel(
    val displayName: String = "",
    val positionText: String = "",
    val avatarUri: String? = null,
    val resumeUrl: String = "",
    val canOpenResume: Boolean = false,
    val isDownloading: Boolean = false,
    val isEmptyProfile: Boolean = false
)

fun UserProfile.toUiModel(isDownloading: Boolean = false): ProfileUiModel {
    val nameText = fullName.trim().ifBlank { "Имя не указано" }
    val posText = position.trim().ifBlank { "Должность не указана" }
    val empty = fullName.isBlank() && position.isBlank() && avatarUri == null && resumeUrl.isBlank()

    return ProfileUiModel(
        displayName = nameText,
        positionText = posText,
        avatarUri = avatarUri,
        resumeUrl = resumeUrl,
        canOpenResume = resumeUrl.isNotBlank(),
        isDownloading = isDownloading,
        isEmptyProfile = empty
    )
}
