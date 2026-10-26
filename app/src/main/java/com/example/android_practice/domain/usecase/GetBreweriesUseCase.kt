package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.model.Brewery
import com.example.android_practice.domain.model.FilterSettings
import com.example.android_practice.domain.repository.BreweryRepository
import javax.inject.Inject

class GetBreweriesUseCase @Inject constructor(
    private val repository: BreweryRepository
) {
    suspend operator fun invoke(settings: FilterSettings = FilterSettings()): List<Brewery> {
        return repository.getBreweries(settings = settings)
    }
}
