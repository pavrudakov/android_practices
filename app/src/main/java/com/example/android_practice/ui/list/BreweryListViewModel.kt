package com.example.android_practice.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.domain.error.HttpStatusException
import com.example.android_practice.domain.error.NetworkUnavailableException
import com.example.android_practice.domain.error.ResourceNotFoundException
import com.example.android_practice.domain.model.FilterSettings
import com.example.android_practice.domain.usecase.ClearSingleFilterUseCase
import com.example.android_practice.domain.usecase.FilterField
import com.example.android_practice.domain.usecase.GetBreweriesUseCase
import com.example.android_practice.domain.usecase.ObserveFilterSettingsUseCase
import com.example.android_practice.domain.usecase.ResetFilterSettingsUseCase
import com.example.android_practice.ui.common.UiState
import com.example.android_practice.ui.list.model.BreweryItemUiModel
import com.example.android_practice.ui.list.model.FilterChipUiModel
import com.example.android_practice.ui.list.model.toChipUiModels
import com.example.android_practice.ui.list.model.toItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreweryListViewModel @Inject constructor(
    private val getBreweriesUseCase: GetBreweriesUseCase,
    private val observeFilterSettingsUseCase: ObserveFilterSettingsUseCase,
    private val clearSingleFilterUseCase: ClearSingleFilterUseCase,
    private val resetFilterSettingsUseCase: ResetFilterSettingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<BreweryItemUiModel>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<BreweryItemUiModel>>> = _uiState.asStateFlow()

    private val _filterSettings = MutableStateFlow(FilterSettings())
    val filterSettings: StateFlow<FilterSettings> = _filterSettings.asStateFlow()

    val activeFilterChips: StateFlow<List<FilterChipUiModel>> = _filterSettings
        .map { it.toChipUiModels() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val showBadge: StateFlow<Boolean> = _filterSettings
        .map { !it.isDefault() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private var fetchJob: Job? = null

    init {
        viewModelScope.launch {
            observeFilterSettingsUseCase().collectLatest { settings ->
                _filterSettings.value = settings
                loadBreweries(settings)
            }
        }
    }

    fun loadBreweries(settings: FilterSettings = _filterSettings.value) {
        fetchJob?.cancel()

        _uiState.value = UiState.Loading

        fetchJob = viewModelScope.launch {
            try {
                val breweries = getBreweriesUseCase(settings = settings)
                val uiModels = breweries.map { it.toItemUiModel() }
                _uiState.value = UiState.Success(uiModels)
            } catch (e: CancellationException) {
                throw e
            } catch (e: NetworkUnavailableException) {
                _uiState.value = UiState.Error("Нет подключения к интернету. Проверьте соединение и попробуйте снова.")
            } catch (e: ResourceNotFoundException) {
                _uiState.value = UiState.Error("Запрашиваемые данные не найдены")
            } catch (e: HttpStatusException) {
                val message = when (e.code) {
                    400 -> "Некорректные параметры поиска (400)"
                    404 -> "Запрашиваемые данные не найдены"
                    in 500..599 -> "Сервер временно недоступен. Попробуйте позже"
                    else -> "Ошибка сервера (${e.code})"
                }
                _uiState.value = UiState.Error(message)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Не удалось загрузить данные. Попробуйте позже.")
            }
        }
    }

    fun clearSingleFilter(field: FilterField) {
        viewModelScope.launch {
            clearSingleFilterUseCase(field)
        }
    }

    fun resetAllFilters() {
        viewModelScope.launch {
            resetFilterSettingsUseCase()
        }
    }
}
