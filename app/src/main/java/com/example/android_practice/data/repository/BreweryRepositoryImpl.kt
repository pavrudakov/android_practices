package com.example.android_practice.data.repository

import com.example.android_practice.data.mapper.toDomain
import com.example.android_practice.data.remote.BreweryApiService
import com.example.android_practice.domain.model.Brewery
import com.example.android_practice.domain.model.FilterSettings
import com.example.android_practice.domain.repository.BreweryRepository
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class BreweryRepositoryImpl @Inject constructor(
    private val apiService: BreweryApiService
) : BreweryRepository {

    private val memoryCache = ConcurrentHashMap<String, List<Brewery>>()

    fun getCachedBreweries(settings: FilterSettings): List<Brewery>? {
        val cacheKey = "${settings.state}_${settings.type}_${settings.name}".lowercase().trim()
        return memoryCache[cacheKey]
    }

    override suspend fun getBreweries(settings: FilterSettings): List<Brewery> {
        val cacheKey = "${settings.state}_${settings.type}_${settings.name}".lowercase().trim()

        val formattedState = settings.state.trim().takeIf { it.isNotBlank() }?.lowercase()?.replace(" ", "_")
        val formattedType = settings.type.trim().takeIf { it.isNotBlank() }?.lowercase()
        val formattedName = settings.name.trim().takeIf { it.isNotBlank() }?.lowercase()

        val remote = apiService.getBreweries(
            state = formattedState,
            type = formattedType,
            name = formattedName,
            perPage = 50
        ).toDomain()

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
