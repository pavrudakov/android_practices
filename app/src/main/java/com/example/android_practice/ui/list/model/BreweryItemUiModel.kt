package com.example.android_practice.ui.list.model

import com.example.android_practice.domain.model.Brewery

data class BreweryItemUiModel(
    val id: String,
    val name: String,
    val breweryType: String,
    val locationText: String
)

fun Brewery.toItemUiModel(): BreweryItemUiModel {
    val location = listOfNotNull(
        city.takeIf { it.isNotBlank() },
        stateProvince.takeIf { it.isNotBlank() }
    ).joinToString(", ")

    return BreweryItemUiModel(
        id = id,
        name = name,
        breweryType = breweryType,
        locationText = location
    )
}
