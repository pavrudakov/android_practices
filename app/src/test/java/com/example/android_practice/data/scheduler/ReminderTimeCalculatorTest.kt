package com.example.android_practice.data.scheduler

import com.example.android_practice.domain.model.ReminderTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class ReminderTimeCalculatorTest {

    @Test
    fun calculateNextTriggerMillis_targetLaterToday_schedulesToday() {
        val now = Calendar.getInstance().apply {
            set(2025, Calendar.MAY, 15, 10, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val targetTime = ReminderTime(18, 30)

        val (triggerMillis, isNextDay) = ReminderTimeCalculator.calculateNextTriggerMillis(
            targetTime,
            now.timeInMillis
        )

        assertFalse(isNextDay)
        val triggerCal = Calendar.getInstance().apply { timeInMillis = triggerMillis }
        assertEquals(2025, triggerCal.get(Calendar.YEAR))
        assertEquals(Calendar.MAY, triggerCal.get(Calendar.MONTH))
        assertEquals(15, triggerCal.get(Calendar.DAY_OF_MONTH))
        assertEquals(18, triggerCal.get(Calendar.HOUR_OF_DAY))
        assertEquals(30, triggerCal.get(Calendar.MINUTE))
        assertEquals(0, triggerCal.get(Calendar.SECOND))
    }

    @Test
    fun calculateNextTriggerMillis_targetEarlierToday_schedulesTomorrow() {
        val now = Calendar.getInstance().apply {
            set(2025, Calendar.MAY, 15, 19, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val targetTime = ReminderTime(18, 30)

        val (triggerMillis, isNextDay) = ReminderTimeCalculator.calculateNextTriggerMillis(
            targetTime,
            now.timeInMillis
        )

        assertTrue(isNextDay)
        val triggerCal = Calendar.getInstance().apply { timeInMillis = triggerMillis }
        assertEquals(2025, triggerCal.get(Calendar.YEAR))
        assertEquals(Calendar.MAY, triggerCal.get(Calendar.MONTH))
        assertEquals(16, triggerCal.get(Calendar.DAY_OF_MONTH))
        assertEquals(18, triggerCal.get(Calendar.HOUR_OF_DAY))
        assertEquals(30, triggerCal.get(Calendar.MINUTE))
        assertEquals(0, triggerCal.get(Calendar.SECOND))
    }

    @Test
    fun calculateNextTriggerMillis_targetExactlyNow_schedulesTomorrow() {
        val now = Calendar.getInstance().apply {
            set(2025, Calendar.MAY, 15, 18, 30, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val targetTime = ReminderTime(18, 30)

        val (triggerMillis, isNextDay) = ReminderTimeCalculator.calculateNextTriggerMillis(
            targetTime,
            now.timeInMillis
        )

        assertTrue(isNextDay)
        val triggerCal = Calendar.getInstance().apply { timeInMillis = triggerMillis }
        assertEquals(16, triggerCal.get(Calendar.DAY_OF_MONTH))
    }
}
