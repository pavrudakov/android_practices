package com.example.android_practice.domain.repository

import com.example.android_practice.domain.model.Brewery
import com.example.android_practice.domain.model.FilterSettings

interface BreweryRepository {
    suspend fun getBreweries(settings: FilterSettings = FilterSettings()): List<Brewery>
    suspend fun getBreweryById(id: String): Brewery?
    suspend fun searchBreweries(query: String): List<Brewery>
}
