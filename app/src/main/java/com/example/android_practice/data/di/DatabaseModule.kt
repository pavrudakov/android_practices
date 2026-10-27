package com.example.android_practice.data.di

import android.content.Context
import androidx.room.Room
import com.example.android_practice.data.local.AppDatabase
import com.example.android_practice.data.local.dao.FavoriteBreweryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "brewery_database.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteBreweryDao(appDatabase: AppDatabase): FavoriteBreweryDao {
        return appDatabase.favoriteBreweryDao()
    }
}
