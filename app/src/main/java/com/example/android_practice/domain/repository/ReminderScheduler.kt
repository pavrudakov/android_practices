package com.example.android_practice.domain.repository

import com.example.android_practice.domain.model.ReminderTime
import com.example.android_practice.domain.model.ScheduleResult

interface ReminderScheduler {
    fun schedule(time: ReminderTime, personName: String): ScheduleResult
    fun cancel()
}
