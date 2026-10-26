package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.model.Brewery
import com.example.android_practice.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFavoritesUseCase @Inject constructor(
    private val repository: FavoritesRepository
) {
    operator fun invoke(): Flow<List<Brewery>> = repository.observeFavorites()
}
