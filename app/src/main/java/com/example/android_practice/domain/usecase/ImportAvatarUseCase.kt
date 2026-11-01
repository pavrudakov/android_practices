package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.repository.AvatarRepository
import javax.inject.Inject

class ImportAvatarUseCase @Inject constructor(
    private val avatarRepository: AvatarRepository
) {
    suspend operator fun invoke(sourceUri: String): String {
        return avatarRepository.importFromUri(sourceUri)
    }
}
