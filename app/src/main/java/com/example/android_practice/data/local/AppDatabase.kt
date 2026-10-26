package com.example.android_practice.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android_practice.data.local.dao.FavoriteBreweryDao
import com.example.android_practice.data.local.entity.FavoriteBreweryEntity

@Database(entities = [FavoriteBreweryEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteBreweryDao(): FavoriteBreweryDao
}
