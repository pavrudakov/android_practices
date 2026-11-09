package com.example.android_practice.domain.error

sealed class DomainException(message: String? = null, cause: Throwable? = null) : Exception(message, cause)

class NetworkUnavailableException(message: String? = null, cause: Throwable? = null) : DomainException(message, cause)

class HttpStatusException(val code: Int, message: String? = null, cause: Throwable? = null) : DomainException(message ?: "HTTP status $code", cause)

class ResourceNotFoundException(message: String? = null, cause: Throwable? = null) : DomainException(message, cause)

class InvalidUrlException(message: String? = null, cause: Throwable? = null) : DomainException(message, cause)

class NotAFileException(message: String? = null, cause: Throwable? = null) : DomainException(message, cause)

class InvalidReminderTimeException(message: String? = null, cause: Throwable? = null) : DomainException(message ?: "Некорректный формат времени", cause)
