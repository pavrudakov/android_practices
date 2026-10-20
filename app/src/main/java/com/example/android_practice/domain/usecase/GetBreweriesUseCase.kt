package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.model.Brewery
import com.example.android_practice.domain.repository.BreweryRepository

class GetBreweriesUseCase(
    private val repository: BreweryRepository
) {
    suspend operator fun invoke(state: String? = null): List<Brewery> {
        return repository.getBreweries(state = state)
    }
}
