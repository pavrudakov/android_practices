package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.model.Brewery
import com.example.android_practice.domain.repository.FavoritesRepository
import javax.inject.Inject

class GetFavoriteByIdUseCase @Inject constructor(
    private val repository: FavoritesRepository
) {
    suspend operator fun invoke(id: String): Brewery? = repository.getFavorite(id)
}
