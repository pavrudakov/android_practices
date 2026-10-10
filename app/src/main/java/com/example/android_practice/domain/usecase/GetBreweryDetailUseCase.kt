package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.model.Brewery
import com.example.android_practice.domain.repository.BreweryRepository

class GetBreweryDetailUseCase(
    private val repository: BreweryRepository
) {
    suspend operator fun invoke(id: String): Brewery? {
        return repository.getBreweryById(id)
    }
}
