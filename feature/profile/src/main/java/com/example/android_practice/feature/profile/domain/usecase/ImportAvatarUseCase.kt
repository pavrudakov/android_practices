package com.example.android_practice.feature.profile.domain.usecase

import com.example.android_practice.feature.profile.domain.repository.AvatarRepository
import javax.inject.Inject

class ImportAvatarUseCase @Inject constructor(
    private val avatarRepository: AvatarRepository
) {
    suspend operator fun invoke(sourceUri: String): String {
        return avatarRepository.importFromUri(sourceUri)
    }
}
