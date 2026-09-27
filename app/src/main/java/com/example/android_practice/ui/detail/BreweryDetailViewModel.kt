package com.example.android_practice.ui.detail

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

class BreweryDetailViewModel(
    private val repository: BreweryRepository = MockBreweryRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Brewery>>(UiState.Loading)
    val uiState: StateFlow<UiState<Brewery>> = _uiState.asStateFlow()

    fun loadBrewery(id: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val brewery = repository.getBreweryById(id)
                if (brewery != null) {
                    _uiState.value = UiState.Success(brewery)
                } else {
                    _uiState.value = UiState.Error("Brewery with ID $id not found")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "An error occurred")
            }
        }
    }
}
