package com.example.android_practice.data.remote

import com.example.android_practice.data.remote.dto.BreweryDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BreweryApiService {
    @GET("v1/breweries")
    suspend fun getBreweries(
        @Query("by_state") state: String? = null,
        @Query("by_type") type: String? = null,
        @Query("by_name") name: String? = null,
        @Query("per_page") perPage: Int = 50,
        @Query("page") page: Int = 1
    ): List<BreweryDto>

    @GET("v1/breweries/{id}")
    suspend fun getBreweryById(@Path("id") id: String): BreweryDto

    @GET("v1/breweries/autocomplete")
    suspend fun searchBreweries(@Query("query") query: String): List<BreweryDto>
}
