package com.example.android_practice.feature.profile.domain.repository

import com.example.android_practice.feature.profile.domain.model.CameraTarget
interface AvatarRepository {
    suspend fun importFromUri(sourceUri: String): String
    suspend fun createCameraTarget(): CameraTarget
    suspend fun delete(uri: String)
}
