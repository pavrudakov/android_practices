package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.model.CameraTarget
import com.example.android_practice.domain.repository.AvatarRepository
import javax.inject.Inject

class PrepareCameraAvatarUseCase @Inject constructor(
    private val avatarRepository: AvatarRepository
) {
    suspend operator fun invoke(): CameraTarget {
        return avatarRepository.createCameraTarget()
    }
}
