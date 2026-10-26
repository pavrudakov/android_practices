package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.model.FilterSettings
import com.example.android_practice.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFilterSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<FilterSettings> = repository.settings
}
