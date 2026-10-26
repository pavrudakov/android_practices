package com.example.android_practice.domain.repository

import com.example.android_practice.domain.model.Brewery
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun observeFavorites(): Flow<List<Brewery>>
    fun observeIsFavorite(id: String): Flow<Boolean>
    suspend fun getFavorite(id: String): Brewery?
    suspend fun add(brewery: Brewery)
    suspend fun remove(id: String)
}
