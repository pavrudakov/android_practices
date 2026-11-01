package com.example.android_practice.data.repository

import com.example.android_practice.data.mapper.toDomain
import com.example.android_practice.data.remote.BreweryApiService
import com.example.android_practice.domain.error.HttpStatusException
import com.example.android_practice.domain.error.NetworkUnavailableException
import com.example.android_practice.domain.error.ResourceNotFoundException
import com.example.android_practice.domain.model.Brewery
import com.example.android_practice.domain.model.FilterSettings
import com.example.android_practice.domain.repository.BreweryRepository
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class BreweryRepositoryImpl @Inject constructor(
    private val apiService: BreweryApiService
) : BreweryRepository {

    private val memoryCache = ConcurrentHashMap<String, List<Brewery>>()

    override fun getCachedBreweries(settings: FilterSettings): List<Brewery>? {
        val cacheKey = "${settings.state}_${settings.type}_${settings.name}".lowercase().trim()
        return memoryCache[cacheKey]
    }

    override suspend fun getBreweries(settings: FilterSettings): List<Brewery> {
        val cacheKey = "${settings.state}_${settings.type}_${settings.name}".lowercase().trim()

        val formattedState = settings.state.trim().takeIf { it.isNotBlank() }?.lowercase()?.replace(" ", "_")
        val formattedType = settings.type.trim().takeIf { it.isNotBlank() }?.lowercase()
        val formattedName = settings.name.trim().takeIf { it.isNotBlank() }?.lowercase()

        try {
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
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            throw NetworkUnavailableException("No network connection", e)
        } catch (e: HttpException) {
            when (e.code()) {
                404 -> throw ResourceNotFoundException("Data not found", e)
                else -> throw HttpStatusException(e.code(), "Server error (${e.code()})", e)
            }
        }
    }

    override suspend fun getBreweryById(id: String): Brewery {
        memoryCache.values.forEach { list ->
            val found = list.find { it.id == id }
            if (found != null) return found
        }
        try {
            return apiService.getBreweryById(id).toDomain()
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            throw NetworkUnavailableException("No network connection", e)
        } catch (e: HttpException) {
            when (e.code()) {
                404 -> throw ResourceNotFoundException("Brewery with ID $id not found", e)
                else -> throw HttpStatusException(e.code(), "Server error (${e.code()})", e)
            }
        }
    }

    override suspend fun searchBreweries(query: String): List<Brewery> {
        try {
            return apiService.searchBreweries(query).toDomain()
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            throw NetworkUnavailableException("No network connection", e)
        } catch (e: HttpException) {
            when (e.code()) {
                404 -> throw ResourceNotFoundException("Search results not found", e)
                else -> throw HttpStatusException(e.code(), "Server error (${e.code()})", e)
            }
        }
    }
}
