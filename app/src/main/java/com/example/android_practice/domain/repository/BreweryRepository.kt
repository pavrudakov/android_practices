package com.example.android_practice.domain.repository

import com.example.android_practice.domain.model.Brewery

interface BreweryRepository {
    suspend fun getBreweries(state: String? = null): List<Brewery>
    suspend fun getBreweryById(id: String): Brewery?
    suspend fun searchBreweries(query: String): List<Brewery>
}
