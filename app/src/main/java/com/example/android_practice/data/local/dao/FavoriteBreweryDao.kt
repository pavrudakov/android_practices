package com.example.android_practice.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android_practice.data.local.entity.FavoriteBreweryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteBreweryDao {
    @Query("SELECT * FROM favorite_breweries")
    fun observeAll(): Flow<List<FavoriteBreweryEntity>>

    @Query("SELECT id FROM favorite_breweries")
    fun observeIds(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_breweries WHERE id = :id)")
    fun observeIsFavorite(id: String): Flow<Boolean>

    @Query("SELECT * FROM favorite_breweries WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): FavoriteBreweryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FavoriteBreweryEntity)

    @Query("DELETE FROM favorite_breweries WHERE id = :id")
    suspend fun deleteById(id: String)
}
