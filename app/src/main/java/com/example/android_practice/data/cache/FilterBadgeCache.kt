package com.example.android_practice.data.cache

import com.example.android_practice.domain.model.FilterSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilterBadgeCache @Inject constructor() {
    private val _showBadge = MutableStateFlow(false)
    val showBadge: StateFlow<Boolean> = _showBadge.asStateFlow()

    fun sync(settings: FilterSettings) {
        _showBadge.value = !settings.isDefault()
    }
}
