package com.example.android_practice.domain.model

data class FilterSettings(
    val state: String = "",
    val type: String = "",
    val name: String = ""
) {
    fun isDefault(): Boolean = state.isBlank() && type.isBlank() && name.isBlank()
}
