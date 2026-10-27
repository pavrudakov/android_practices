package com.example.android_practice.data.repository

import com.example.android_practice.data.local.dao.FavoriteBreweryDao
import com.example.android_practice.data.mapper.toDomain
import com.example.android_practice.data.mapper.toEntity
import com.example.android_practice.domain.model.Brewery
import com.example.android_practice.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val favoriteBreweryDao: FavoriteBreweryDao
) : FavoritesRepository {

    override fun observeFavorites(): Flow<List<Brewery>> {
        return favoriteBreweryDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeIsFavorite(id: String): Flow<Boolean> {
        return favoriteBreweryDao.observeIsFavorite(id)
    }

    override suspend fun getFavorite(id: String): Brewery? {
        return favoriteBreweryDao.getById(id)?.toDomain()
    }

    override suspend fun add(brewery: Brewery) {
        favoriteBreweryDao.insert(brewery.toEntity())
    }

    override suspend fun remove(id: String) {
        favoriteBreweryDao.deleteById(id)
    }
}
