package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.model.UserProfile
import com.example.android_practice.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke(): Flow<UserProfile> = profileRepository.profile
}
