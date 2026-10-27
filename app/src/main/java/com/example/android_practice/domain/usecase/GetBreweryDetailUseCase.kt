package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.model.Brewery
import com.example.android_practice.domain.repository.BreweryRepository
import com.example.android_practice.domain.repository.FavoritesRepository
import javax.inject.Inject

class GetBreweryDetailUseCase @Inject constructor(
    private val repository: BreweryRepository,
    private val favoritesRepository: FavoritesRepository
) {
    suspend operator fun invoke(id: String): Brewery? {
        val favorite = favoritesRepository.getFavorite(id)
        return try {
            repository.getBreweryById(id) ?: favorite
        } catch (e: Exception) {
            favorite ?: throw e
        }
    }
}
