package com.example.android_practice.feature.profile.domain.model

data class ScheduleResult(
    val triggerAtMillis: Long,
    val isExact: Boolean,
    val isNextDay: Boolean
)
