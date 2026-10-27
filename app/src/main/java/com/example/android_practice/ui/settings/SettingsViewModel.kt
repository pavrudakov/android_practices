package com.example.android_practice.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.data.cache.FilterBadgeCache
import com.example.android_practice.domain.model.FilterSettings
import com.example.android_practice.domain.usecase.ObserveFilterSettingsUseCase
import com.example.android_practice.domain.usecase.ResetFilterSettingsUseCase
import com.example.android_practice.domain.usecase.SaveFilterSettingsUseCase
import com.example.android_practice.domain.usecase.SearchBreweriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val observeFilterSettingsUseCase: ObserveFilterSettingsUseCase,
    private val saveFilterSettingsUseCase: SaveFilterSettingsUseCase,
    private val resetFilterSettingsUseCase: ResetFilterSettingsUseCase,
    private val searchBreweriesUseCase: SearchBreweriesUseCase,
    private val filterBadgeCache: FilterBadgeCache
) : ViewModel() {

    private val _draftSettings = MutableStateFlow(FilterSettings())
    val draftSettings: StateFlow<FilterSettings> = _draftSettings.asStateFlow()

    private val _nameSuggestions = MutableStateFlow<List<String>>(emptyList())
    val nameSuggestions: StateFlow<List<String>> = _nameSuggestions.asStateFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            val current = observeFilterSettingsUseCase().first()
            _draftSettings.value = current
            filterBadgeCache.sync(current)
        }
    }

    fun updateState(state: String) {
        _draftSettings.value = _draftSettings.value.copy(state = state)
    }

    fun updateType(type: String) {
        _draftSettings.value = _draftSettings.value.copy(type = type)
    }

    fun updateName(name: String) {
        _draftSettings.value = _draftSettings.value.copy(name = name)

        searchJob?.cancel()
        if (name.trim().length >= 2) {
            searchJob = viewModelScope.launch {
                delay(300)
                try {
                    val results = searchBreweriesUseCase(name.trim())
                    val suggestions = results.map { it.name }.distinct().take(6)
                    _nameSuggestions.value = suggestions
                } catch (e: Exception) {
                    _nameSuggestions.value = emptyList()
                }
            }
        } else {
            _nameSuggestions.value = emptyList()
        }
    }

    fun clearNameSuggestions() {
        _nameSuggestions.value = emptyList()
    }

    fun selectNameSuggestion(suggestion: String) {
        _draftSettings.value = _draftSettings.value.copy(name = suggestion)
        _nameSuggestions.value = emptyList()
    }

    fun applyAndSave(onDone: () -> Unit) {
        viewModelScope.launch {
            val toSave = _draftSettings.value
            saveFilterSettingsUseCase(toSave)
            filterBadgeCache.sync(toSave)
            onDone()
        }
    }

    fun reset() {
        viewModelScope.launch {
            _draftSettings.value = FilterSettings()
            _nameSuggestions.value = emptyList()
            resetFilterSettingsUseCase()
            filterBadgeCache.sync(FilterSettings())
        }
    }
}
