package com.example.android_practice

import android.app.Application
import com.example.android_practice.feature.profile.ProfileFeature
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BreweryApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ProfileFeature.initialize(this)
    }
}
