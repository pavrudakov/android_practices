package com.example.android_practice.feature.profile.domain.usecase

import com.example.android_practice.feature.profile.domain.repository.AvatarRepository
import javax.inject.Inject

class DiscardAvatarUseCase @Inject constructor(
    private val avatarRepository: AvatarRepository
) {
    suspend operator fun invoke(uri: String) {
        avatarRepository.delete(uri)
    }
}
