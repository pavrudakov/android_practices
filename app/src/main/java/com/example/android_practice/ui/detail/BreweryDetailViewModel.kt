package com.example.android_practice.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.domain.error.HttpStatusException
import com.example.android_practice.domain.error.NetworkUnavailableException
import com.example.android_practice.domain.error.ResourceNotFoundException
import com.example.android_practice.domain.model.Brewery
import com.example.android_practice.domain.usecase.GetBreweryDetailUseCase
import com.example.android_practice.domain.usecase.ObserveIsFavoriteUseCase
import com.example.android_practice.domain.usecase.ToggleFavoriteUseCase
import com.example.android_practice.ui.common.UiState
import com.example.android_practice.ui.detail.model.BreweryDetailUiModel
import com.example.android_practice.ui.detail.model.toDetailUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreweryDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getBreweryDetailUseCase: GetBreweryDetailUseCase,
    private val observeIsFavoriteUseCase: ObserveIsFavoriteUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val breweryId: String = savedStateHandle.get<String>("breweryId") ?: ""

    private val _uiState = MutableStateFlow<UiState<BreweryDetailUiModel>>(UiState.Loading)
    val uiState: StateFlow<UiState<BreweryDetailUiModel>> = _uiState.asStateFlow()

    val isFavorite: StateFlow<Boolean> = observeIsFavoriteUseCase(breweryId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private var currentBrewery: Brewery? = null
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
                    currentBrewery = brewery
                    _uiState.value = UiState.Success(brewery.toDetailUiModel())
                } else {
                    _uiState.value = UiState.Error("Пивоварня с ID $id не найдена")
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: NetworkUnavailableException) {
                _uiState.value = UiState.Error("Нет подключения к интернету. Проверьте соединение и попробуйте снова.")
            } catch (e: ResourceNotFoundException) {
                _uiState.value = UiState.Error("Пивоварня не найдена")
            } catch (e: HttpStatusException) {
                val message = when (e.code) {
                    400 -> "Некорректный запрос к серверу (400)"
                    404 -> "Пивоварня не найдена на сервере"
                    in 500..599 -> "Сервер временно недоступен. Попробуйте позже"
                    else -> "Ошибка сервера (${e.code})"
                }
                _uiState.value = UiState.Error(message)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Не удалось загрузить данные. Попробуйте позже.")
            }
        }
    }

    fun toggleFavorite() {
        val brewery = currentBrewery ?: return
        viewModelScope.launch {
            toggleFavoriteUseCase(brewery)
        }
    }
}
