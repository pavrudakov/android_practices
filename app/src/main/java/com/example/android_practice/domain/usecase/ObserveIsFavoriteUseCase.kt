package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveIsFavoriteUseCase @Inject constructor(
    private val repository: FavoritesRepository
) {
    operator fun invoke(id: String): Flow<Boolean> = repository.observeIsFavorite(id)
}
