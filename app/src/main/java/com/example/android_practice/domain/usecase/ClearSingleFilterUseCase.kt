package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

enum class FilterField { STATE, TYPE, NAME }

class ClearSingleFilterUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(field: FilterField) {
        val current = repository.settings.first()
        val updated = when (field) {
            FilterField.STATE -> current.copy(state = "")
            FilterField.TYPE -> current.copy(type = "")
            FilterField.NAME -> current.copy(name = "")
        }
        repository.save(updated)
    }
}
