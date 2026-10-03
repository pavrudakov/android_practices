package com.example.android_practice.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.data.model.Brewery
import com.example.android_practice.data.repository.BreweryRepository
import com.example.android_practice.data.repository.MockBreweryRepository
import com.example.android_practice.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BreweryListViewModel(
    private val repository: BreweryRepository = MockBreweryRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Brewery>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Brewery>>> = _uiState.asStateFlow()

    init {
        loadBreweries()
    }

    fun loadBreweries() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val breweries = repository.getBreweries()
                _uiState.value = UiState.Success(breweries)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to load breweries")
            }
        }
    }
}
