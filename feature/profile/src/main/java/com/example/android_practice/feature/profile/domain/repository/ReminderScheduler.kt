package com.example.android_practice.feature.profile.domain.repository

import com.example.android_practice.feature.profile.domain.model.ReminderTime
import com.example.android_practice.feature.profile.domain.model.ScheduleResult
interface ReminderScheduler {
    fun schedule(time: ReminderTime, personName: String): ScheduleResult
    fun cancel()
}
