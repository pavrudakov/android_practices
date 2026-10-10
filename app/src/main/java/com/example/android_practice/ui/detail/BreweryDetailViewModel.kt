package com.example.android_practice.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.data.di.NetworkModule
import com.example.android_practice.domain.model.Brewery
import com.example.android_practice.domain.usecase.GetBreweryDetailUseCase
import com.example.android_practice.ui.common.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class BreweryDetailViewModel(
    private val getBreweryDetailUseCase: GetBreweryDetailUseCase = NetworkModule.getBreweryDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Brewery>>(UiState.Loading)
    val uiState: StateFlow<UiState<Brewery>> = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    fun loadBrewery(id: String) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val brewery = getBreweryDetailUseCase(id)
                if (brewery != null) {
                    _uiState.value = UiState.Success(brewery)
                } else {
                    _uiState.value = UiState.Error("Пивоварня с ID $id не найдена")
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                _uiState.value = UiState.Error("Ошибка сети: ${e.localizedMessage ?: "Таймаут подключения"}")
            } catch (e: HttpException) {
                _uiState.value = UiState.Error("Ошибка сервера: ${e.code()}")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Что-то пошло не так: ${e.localizedMessage ?: "Неизвестная ошибка"}")
            }
        }
    }
}
