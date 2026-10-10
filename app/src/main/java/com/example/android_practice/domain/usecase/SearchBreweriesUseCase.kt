package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.model.Brewery
import com.example.android_practice.domain.repository.BreweryRepository

class SearchBreweriesUseCase(
    private val repository: BreweryRepository
) {
    suspend operator fun invoke(query: String): List<Brewery> {
        return repository.searchBreweries(query)
    }
}
