package com.example.android_practice.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.domain.usecase.ObserveFavoritesUseCase
import com.example.android_practice.ui.list.model.BreweryItemUiModel
import com.example.android_practice.ui.list.model.toItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    observeFavoritesUseCase: ObserveFavoritesUseCase
) : ViewModel() {

    val favorites: StateFlow<List<BreweryItemUiModel>> = observeFavoritesUseCase()
        .map { list -> list.map { it.toItemUiModel() } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
