package com.example.android_practice.domain.model

data class UserProfile(
    val fullName: String = "",
    val position: String = "",
    val avatarUri: String? = null,
    val resumeUrl: String = ""
)
