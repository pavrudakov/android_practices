package com.example.android_practice.feature.profile.domain.usecase

import com.example.android_practice.feature.profile.domain.repository.ReminderScheduler
import javax.inject.Inject

class CancelReminderUseCase @Inject constructor(
    private val reminderScheduler: ReminderScheduler
) {
    operator fun invoke() {
        reminderScheduler.cancel()
    }
}
