package com.example.android_practice.ui.profile.edit

import org.junit.Assert.assertEquals
import org.junit.Test

class ProfileEditViewModelTest {

    @Test
    fun testFormatTimeInput_typingSingleDigitThreeToNine_autoPrefixesZeroAndAppendsColon() {
        val r3 = ProfileEditViewModel.formatTimeInput("3", "")
        assertEquals("03:", r3)

        val r5 = ProfileEditViewModel.formatTimeInput("5", "")
        assertEquals("05:", r5)

        val r9 = ProfileEditViewModel.formatTimeInput("9", "")
        assertEquals("09:", r9)

        val rReplaced = ProfileEditViewModel.formatTimeInput("5", "18:30")
        assertEquals("05:", rReplaced)
    }

    @Test
    fun testFormatTimeInput_typingSingleDigitZeroToOneToTwo_doesNotAutoPrefixZero() {
        val r0 = ProfileEditViewModel.formatTimeInput("0", "")
        assertEquals("0", r0)

        val r1 = ProfileEditViewModel.formatTimeInput("1", "")
        assertEquals("1", r1)

        val r2 = ProfileEditViewModel.formatTimeInput("2", "")
        assertEquals("2", r2)
    }

    @Test
    fun testFormatTimeInput_typingTwoDigits_appendsColon() {
        val result = ProfileEditViewModel.formatTimeInput("18", "1")
        assertEquals("18:", result)
    }

    @Test
    fun testFormatTimeInput_typingAfterColon_appendsMinutes() {
        val r1 = ProfileEditViewModel.formatTimeInput("18:3", "18:")
        assertEquals("18:3", r1)

        val r2 = ProfileEditViewModel.formatTimeInput("18:30", "18:3")
        assertEquals("18:30", r2)
    }

    @Test
    fun testFormatTimeInput_typingExcessiveDigits_limitsToTwoDigitsAfterColon() {
        val result = ProfileEditViewModel.formatTimeInput("18:305", "18:30")
        assertEquals("18:30", result)
    }

    @Test
    fun testFormatTimeInput_typingExcessiveDigitsWithoutColon_formatsToHHMM() {
        val result = ProfileEditViewModel.formatTimeInput("18305", "1")
        assertEquals("18:30", result)
    }

    @Test
    fun testFormatTimeInput_backspacingColon_doesNotReAddColon() {
        val result = ProfileEditViewModel.formatTimeInput("18", "18:")
        assertEquals("18", result)
    }

    @Test
    fun testFormatTimeInput_backspacingAutoPrefixedTime_clearsToEmpty() {
        val result = ProfileEditViewModel.formatTimeInput("03", "03:")
        assertEquals("", result)
    }

    @Test
    fun testFormatTimeInput_clearingInput_returnsEmptyString() {
        val result = ProfileEditViewModel.formatTimeInput("", "18:30")
        assertEquals("", result)
    }
}
