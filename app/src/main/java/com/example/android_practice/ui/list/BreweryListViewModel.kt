package com.example.android_practice.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.data.cache.FilterBadgeCache
import com.example.android_practice.domain.model.Brewery
import com.example.android_practice.domain.model.FilterSettings
import com.example.android_practice.domain.usecase.ClearSingleFilterUseCase
import com.example.android_practice.domain.usecase.FilterField
import com.example.android_practice.domain.usecase.GetBreweriesUseCase
import com.example.android_practice.domain.usecase.ObserveFilterSettingsUseCase
import com.example.android_practice.domain.usecase.ResetFilterSettingsUseCase
import com.example.android_practice.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BreweryListViewModel @Inject constructor(
    private val getBreweriesUseCase: GetBreweriesUseCase,
    private val observeFilterSettingsUseCase: ObserveFilterSettingsUseCase,
    private val clearSingleFilterUseCase: ClearSingleFilterUseCase,
    private val resetFilterSettingsUseCase: ResetFilterSettingsUseCase,
    private val filterBadgeCache: FilterBadgeCache
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Brewery>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Brewery>>> = _uiState.asStateFlow()

    private val _filterSettings = MutableStateFlow(FilterSettings())
    val filterSettings: StateFlow<FilterSettings> = _filterSettings.asStateFlow()

    val showBadge: StateFlow<Boolean> = filterBadgeCache.showBadge

    private var fetchJob: Job? = null

    init {
        viewModelScope.launch {
            observeFilterSettingsUseCase().collectLatest { settings ->
                _filterSettings.value = settings
                filterBadgeCache.sync(settings)
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
                _uiState.value = UiState.Success(breweries)
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                _uiState.value = UiState.Error("Нет подключения к интернету. Проверьте соединение и попробуйте снова.")
            } catch (e: HttpException) {
                val message = when (e.code()) {
                    404 -> "Запрашиваемые данные не найдены"
                    in 500..599 -> "Сервер временно недоступен. Попробуйте позже"
                    else -> "Ошибка сервера (${e.code()})"
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
