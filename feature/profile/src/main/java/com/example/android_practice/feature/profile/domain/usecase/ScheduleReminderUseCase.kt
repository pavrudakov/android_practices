package com.example.android_practice.feature.profile.domain.usecase

import com.example.android_practice.feature.profile.domain.model.ReminderTime
import com.example.android_practice.feature.profile.domain.model.ScheduleResult
import com.example.android_practice.feature.profile.domain.repository.ReminderScheduler
import javax.inject.Inject

class ScheduleReminderUseCase @Inject constructor(
    private val reminderScheduler: ReminderScheduler
) {
    operator fun invoke(time: ReminderTime, personName: String): ScheduleResult {
        return reminderScheduler.schedule(time, personName)
    }
}
