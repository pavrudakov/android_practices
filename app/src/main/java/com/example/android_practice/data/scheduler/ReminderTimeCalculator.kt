package com.example.android_practice.data.scheduler

import com.example.android_practice.domain.model.ReminderTime
import java.util.Calendar

object ReminderTimeCalculator {
    fun calculateNextTriggerMillis(time: ReminderTime, nowMillis: Long): Pair<Long, Boolean> {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = nowMillis
            set(Calendar.HOUR_OF_DAY, time.hour)
            set(Calendar.MINUTE, time.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        var isNextDay = false
        if (calendar.timeInMillis <= nowMillis) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            isNextDay = true
        }
        return Pair(calendar.timeInMillis, isNextDay)
    }
}
