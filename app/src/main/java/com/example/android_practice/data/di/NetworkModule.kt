package com.example.android_practice.data.di

import com.example.android_practice.data.remote.BreweryApiService
import com.example.android_practice.data.repository.BreweryRepositoryImpl
import com.example.android_practice.domain.repository.BreweryRepository
import com.example.android_practice.domain.usecase.GetBreweriesUseCase
import com.example.android_practice.domain.usecase.GetBreweryDetailUseCase
import com.example.android_practice.domain.usecase.SearchBreweriesUseCase
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {
    private const val BASE_URL = "https://api.openbrewerydb.org/"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    val apiService: BreweryApiService by lazy {
        retrofit.create(BreweryApiService::class.java)
    }

    val breweryRepository: BreweryRepository by lazy {
        BreweryRepositoryImpl(apiService)
    }

    val getBreweriesUseCase: GetBreweriesUseCase by lazy {
        GetBreweriesUseCase(breweryRepository)
    }

    val getBreweryDetailUseCase: GetBreweryDetailUseCase by lazy {
        GetBreweryDetailUseCase(breweryRepository)
    }

    val searchBreweriesUseCase: SearchBreweriesUseCase by lazy {
        SearchBreweriesUseCase(breweryRepository)
    }
}
