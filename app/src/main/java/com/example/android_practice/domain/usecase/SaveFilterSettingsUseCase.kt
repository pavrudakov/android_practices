package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.model.FilterSettings
import com.example.android_practice.domain.repository.SettingsRepository
import javax.inject.Inject

class SaveFilterSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(settings: FilterSettings) {
        repository.save(settings)
    }
}
