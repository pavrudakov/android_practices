package com.example.android_practice.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.data.di.NetworkModule
import com.example.android_practice.data.repository.BreweryRepositoryImpl
import com.example.android_practice.domain.model.Brewery
import com.example.android_practice.domain.usecase.GetBreweriesUseCase
import com.example.android_practice.ui.common.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class BreweryListViewModel(
    private val getBreweriesUseCase: GetBreweriesUseCase = NetworkModule.getBreweriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Brewery>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Brewery>>> = _uiState.asStateFlow()

    private val _selectedState = MutableStateFlow<String?>(null)
    val selectedState: StateFlow<String?> = _selectedState.asStateFlow()

    private var fetchJob: Job? = null

    init {
        loadBreweries()
    }

    fun loadBreweries(state: String? = _selectedState.value) {
        _selectedState.value = state
        fetchJob?.cancel()

        val cached = (NetworkModule.breweryRepository as? BreweryRepositoryImpl)?.getCachedBreweries(state)
        if (!cached.isNullOrEmpty()) {
            _uiState.value = UiState.Success(cached)
        } else {
            _uiState.value = UiState.Loading
        }

        fetchJob = viewModelScope.launch {
            try {
                val breweries = getBreweriesUseCase(state = state)
                _uiState.value = UiState.Success(breweries)
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                if (_uiState.value !is UiState.Success) {
                    _uiState.value = UiState.Error("Ошибка сети: ${e.localizedMessage ?: "Таймаут подключения"}")
                }
            } catch (e: HttpException) {
                if (_uiState.value !is UiState.Success) {
                    _uiState.value = UiState.Error("Ошибка сервера: ${e.code()}")
                }
            } catch (e: Exception) {
                if (_uiState.value !is UiState.Success) {
                    _uiState.value = UiState.Error("Что-то пошло не так: ${e.localizedMessage ?: "Неизвестная ошибка"}")
                }
            }
        }
    }
}
