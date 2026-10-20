package com.example.android_practice.data.repository

import com.example.android_practice.data.mapper.toDomain
import com.example.android_practice.data.remote.BreweryApiService
import com.example.android_practice.domain.model.Brewery
import com.example.android_practice.domain.repository.BreweryRepository
import java.util.concurrent.ConcurrentHashMap

class BreweryRepositoryImpl(
    private val apiService: BreweryApiService
) : BreweryRepository {

    private val memoryCache = ConcurrentHashMap<String, List<Brewery>>()

    fun getCachedBreweries(state: String?): List<Brewery>? {
        val cacheKey = state?.lowercase()?.trim() ?: "ALL"
        return memoryCache[cacheKey]
    }

    override suspend fun getBreweries(state: String?): List<Brewery> {
        val cacheKey = state?.lowercase()?.trim() ?: "ALL"
        val formattedState = state?.lowercase()?.replace(" ", "_")
        
        val remote = apiService.getBreweries(state = formattedState, perPage = 50).toDomain()
        if (remote.isNotEmpty()) {
            memoryCache[cacheKey] = remote
        }
        return remote
    }

    override suspend fun getBreweryById(id: String): Brewery? {
        memoryCache.values.forEach { list ->
            val found = list.find { it.id == id }
            if (found != null) return found
        }
        return try {
            apiService.getBreweryById(id).toDomain()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun searchBreweries(query: String): List<Brewery> {
        return apiService.searchBreweries(query).toDomain()
    }
}
