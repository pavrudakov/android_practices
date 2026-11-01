package com.example.android_practice.ui.list.model

import com.example.android_practice.domain.model.FilterSettings
import com.example.android_practice.domain.usecase.FilterField

data class FilterChipUiModel(
    val field: FilterField,
    val text: String
)

fun FilterSettings.toChipUiModels(): List<FilterChipUiModel> {
    val chips = mutableListOf<FilterChipUiModel>()
    if (state.isNotBlank()) {
        chips.add(FilterChipUiModel(FilterField.STATE, "Штат: $state"))
    }
    if (type.isNotBlank()) {
        chips.add(FilterChipUiModel(FilterField.TYPE, "Тип: $type"))
    }
    if (name.isNotBlank()) {
        chips.add(FilterChipUiModel(FilterField.NAME, "Название: $name"))
    }
    return chips
}
