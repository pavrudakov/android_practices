package com.example.android_practice.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ReminderTimeTest {

    @Test
    fun parse_validTimes_returnsReminderTime() {
        val t1 = ReminderTime.parse("00:00")
        assertNotNull(t1)
        assertEquals(0, t1?.hour)
        assertEquals(0, t1?.minute)
        assertEquals("00:00", t1?.format())

        val t2 = ReminderTime.parse("23:59")
        assertNotNull(t2)
        assertEquals(23, t2?.hour)
        assertEquals(59, t2?.minute)
        assertEquals("23:59", t2?.format())

        val t3 = ReminderTime.parse("18:30")
        assertNotNull(t3)
        assertEquals(18, t3?.hour)
        assertEquals(30, t3?.minute)
        assertEquals("18:30", t3?.format())

        val t4 = ReminderTime.parse("  07:05  ")
        assertNotNull(t4)
        assertEquals(7, t4?.hour)
        assertEquals(5, t4?.minute)
        assertEquals("07:05", t4?.format())
    }

    @Test
    fun parse_invalidTimes_returnsNull() {
        assertNull(ReminderTime.parse("24:00"))
        assertNull(ReminderTime.parse("7:5"))
        assertNull(ReminderTime.parse("25:99"))
        assertNull(ReminderTime.parse("12-30"))
        assertNull(ReminderTime.parse("abc"))
        assertNull(ReminderTime.parse(""))
        assertNull(ReminderTime.parse("   "))
        assertNull(ReminderTime.parse("18:30:00"))
    }
}
