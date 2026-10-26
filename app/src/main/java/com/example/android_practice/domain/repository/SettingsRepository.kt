package com.example.android_practice.domain.repository

import com.example.android_practice.domain.model.FilterSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settings: Flow<FilterSettings>
    suspend fun save(settings: FilterSettings)
    suspend fun reset()
}
