package com.example.android_practice.ui.detail.model

import com.example.android_practice.domain.model.Brewery

data class BreweryDetailUiModel(
    val id: String,
    val name: String,
    val breweryType: String,
    val streetText: String?,
    val addressText: String,
    val countryText: String,
    val phoneText: String,
    val rawPhone: String?,
    val websiteText: String,
    val rawWebsite: String?,
    val coordinatesText: String?
)

fun Brewery.toDetailUiModel(): BreweryDetailUiModel {
    val cityAndState = listOfNotNull(
        city.takeIf { it.isNotBlank() },
        stateProvince.takeIf { it.isNotBlank() }
    ).joinToString(", ")

    val fullAddress = if (postalCode.isNotBlank()) {
        if (cityAndState.isNotBlank()) "$cityAndState $postalCode" else postalCode
    } else {
        cityAndState
    }

    val coords = if (latitude != null && longitude != null) {
        "Широта: $latitude\nДолгота: $longitude"
    } else {
        null
    }

    return BreweryDetailUiModel(
        id = id,
        name = name,
        breweryType = breweryType,
        streetText = street.takeIf { !it.isNullOrBlank() },
        addressText = fullAddress,
        countryText = country,
        phoneText = if (!phone.isNullOrBlank()) phone else "Телефон: нет данных",
        rawPhone = phone.takeIf { !it.isNullOrBlank() },
        websiteText = if (!websiteUrl.isNullOrBlank()) websiteUrl else "Веб-сайт: нет данных",
        rawWebsite = websiteUrl.takeIf { !it.isNullOrBlank() },
        coordinatesText = coords
    )
}
