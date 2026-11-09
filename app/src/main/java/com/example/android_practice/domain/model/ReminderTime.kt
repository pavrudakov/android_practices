package com.example.android_practice.domain.model

data class ReminderTime(
    val hour: Int,
    val minute: Int
) {
    fun format(): String {
        return String.format("%02d:%02d", hour, minute)
    }

    companion object {
        private val REGEX = Regex("^([01]\\d|2[0-3]):[0-5]\\d$")

        fun parse(text: String): ReminderTime? {
            val trimmed = text.trim()
            if (!REGEX.matches(trimmed)) return null
            val parts = trimmed.split(":")
            if (parts.size != 2) return null
            val h = parts[0].toIntOrNull() ?: return null
            val m = parts[1].toIntOrNull() ?: return null
            if (h !in 0..23 || m !in 0..59) return null
            return ReminderTime(h, m)
        }
    }
}
