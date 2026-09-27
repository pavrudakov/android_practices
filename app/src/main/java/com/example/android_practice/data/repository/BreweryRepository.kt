package com.example.android_practice.data.repository

import com.example.android_practice.data.model.Brewery

interface BreweryRepository {
    suspend fun getBreweries(): List<Brewery>
    suspend fun getBreweryById(id: String): Brewery?
}
