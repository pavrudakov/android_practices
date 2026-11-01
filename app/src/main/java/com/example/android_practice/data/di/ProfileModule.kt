package com.example.android_practice.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.android_practice.data.remote.DownloadApiService
import com.example.android_practice.data.repository.AvatarRepositoryImpl
import com.example.android_practice.data.repository.ProfileRepositoryImpl
import com.example.android_practice.data.repository.ResumeRepositoryImpl
import com.example.android_practice.domain.repository.AvatarRepository
import com.example.android_practice.domain.repository.ProfileRepository
import com.example.android_practice.domain.repository.ResumeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProfileDataStore

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        impl: ProfileRepositoryImpl
    ): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindAvatarRepository(
        impl: AvatarRepositoryImpl
    ): AvatarRepository

    @Binds
    @Singleton
    abstract fun bindResumeRepository(
        impl: ResumeRepositoryImpl
    ): ResumeRepository
}

@Module
@InstallIn(SingletonComponent::class)
object ProfileDataModule {

    @Provides
    @Singleton
    @ProfileDataStore
    fun provideProfileDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("profile_preferences") }
        )
    }

    @Provides
    @Singleton
    @Named("downloadClient")
    fun provideDownloadOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .followRedirects(true)
            .followSslRedirects(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideDownloadApiService(@Named("downloadClient") client: OkHttpClient): DownloadApiService {
        return Retrofit.Builder()
            .baseUrl("https://localhost/")
            .client(client)
            .build()
            .create(DownloadApiService::class.java)
    }
}
