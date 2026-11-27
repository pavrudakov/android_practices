package com.example.android_practice.feature.profile.domain.error

import com.example.android_practice.core.common.error.DomainException

class InvalidUrlException(message: String? = null, cause: Throwable? = null) : DomainException(message, cause)

class NotAFileException(message: String? = null, cause: Throwable? = null) : DomainException(message, cause)

class InvalidReminderTimeException(message: String? = null, cause: Throwable? = null) : DomainException(message ?: "Некорректный формат времени", cause)
