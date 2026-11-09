package com.example.android_practice.domain.model

data class ScheduleResult(
    val triggerAtMillis: Long,
    val isExact: Boolean,
    val isNextDay: Boolean
)
