package com.example.android_practice.data.di

import com.example.android_practice.data.repository.BreweryRepositoryImpl
import com.example.android_practice.data.repository.FavoritesRepositoryImpl
import com.example.android_practice.data.repository.SettingsRepositoryImpl
import com.example.android_practice.domain.repository.BreweryRepository
import com.example.android_practice.domain.repository.FavoritesRepository
import com.example.android_practice.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBreweryRepository(
        impl: BreweryRepositoryImpl
    ): BreweryRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(
        impl: FavoritesRepositoryImpl
    ): FavoritesRepository
}
