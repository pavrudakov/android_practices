package com.example.android_practice.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface DownloadApiService {
    @Streaming
    @GET
    suspend fun downloadFile(@Url url: String): ResponseBody
}
