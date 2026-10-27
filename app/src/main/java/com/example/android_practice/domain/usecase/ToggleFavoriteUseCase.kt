package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.model.Brewery
import com.example.android_practice.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: FavoritesRepository
) {
    suspend operator fun invoke(brewery: Brewery) {
        val isFav = repository.observeIsFavorite(brewery.id).first()
        if (isFav) {
            repository.remove(brewery.id)
        } else {
            repository.add(brewery)
        }
    }
}
