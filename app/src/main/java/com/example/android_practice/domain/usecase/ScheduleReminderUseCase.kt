package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.model.ReminderTime
import com.example.android_practice.domain.model.ScheduleResult
import com.example.android_practice.domain.repository.ReminderScheduler
import javax.inject.Inject

class ScheduleReminderUseCase @Inject constructor(
    private val reminderScheduler: ReminderScheduler
) {
    operator fun invoke(time: ReminderTime, personName: String): ScheduleResult {
        return reminderScheduler.schedule(time, personName)
    }
}
