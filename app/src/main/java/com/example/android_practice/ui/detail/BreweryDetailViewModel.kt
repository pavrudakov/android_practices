package com.example.android_practice.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.domain.model.Brewery
import com.example.android_practice.domain.usecase.GetBreweryDetailUseCase
import com.example.android_practice.domain.usecase.ObserveIsFavoriteUseCase
import com.example.android_practice.domain.usecase.ToggleFavoriteUseCase
import com.example.android_practice.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BreweryDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getBreweryDetailUseCase: GetBreweryDetailUseCase,
    private val observeIsFavoriteUseCase: ObserveIsFavoriteUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val breweryId: String = savedStateHandle.get<String>("breweryId") ?: ""

    private val _uiState = MutableStateFlow<UiState<Brewery>>(UiState.Loading)
    val uiState: StateFlow<UiState<Brewery>> = _uiState.asStateFlow()

    val isFavorite: StateFlow<Boolean> = observeIsFavoriteUseCase(breweryId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private var fetchJob: Job? = null

    init {
        if (breweryId.isNotBlank()) {
            loadBrewery(breweryId)
        } else {
            _uiState.value = UiState.Error("Идентификатор пивоварни не указан")
        }
    }

    fun loadBrewery(id: String = breweryId) {
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
                _uiState.value = UiState.Error("Нет подключения к интернету. Проверьте соединение и попробуйте снова.")
            } catch (e: HttpException) {
                val message = when (e.code()) {
                    404 -> "Пивоварня не найдена на сервере"
                    in 500..599 -> "Сервер временно недоступен. Попробуйте позже"
                    else -> "Ошибка сервера (${e.code()})"
                }
                _uiState.value = UiState.Error(message)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Не удалось загрузить данные. Попробуйте позже.")
            }
        }
    }

    fun toggleFavorite() {
        val current = (_uiState.value as? UiState.Success)?.data ?: return
        viewModelScope.launch {
            toggleFavoriteUseCase(current)
        }
    }
}
