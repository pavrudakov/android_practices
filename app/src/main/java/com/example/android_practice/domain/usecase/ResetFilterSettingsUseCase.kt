package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.repository.SettingsRepository
import javax.inject.Inject

class ResetFilterSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke() {
        repository.reset()
    }
}
