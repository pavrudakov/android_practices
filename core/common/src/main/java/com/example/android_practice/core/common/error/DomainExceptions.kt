package com.example.android_practice.core.common.error

/** Базовый тип доменных ошибок (общий для всех модулей). */
open class DomainException(message: String? = null, cause: Throwable? = null) : Exception(message, cause)

/** Нет сети / таймаут. */
class NetworkUnavailableException(message: String? = null, cause: Throwable? = null) : DomainException(message, cause)

/** Сервер ответил кодом не из диапазона 2xx. */
class HttpStatusException(val code: Int, message: String? = null, cause: Throwable? = null) : DomainException(message ?: "HTTP status $code", cause)

/** Ресурс не найден (HTTP 404). */
class ResourceNotFoundException(message: String? = null, cause: Throwable? = null) : DomainException(message, cause)
