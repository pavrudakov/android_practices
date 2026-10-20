package com.example.android_practice.data.mapper

import com.example.android_practice.data.remote.dto.BreweryDto
import com.example.android_practice.domain.model.Brewery

fun BreweryDto.toDomain(): Brewery {
    return Brewery(
        id = id.orEmpty(),
        name = name ?: "Без названия",
        breweryType = breweryType ?: "micro",
        street = street,
        city = city ?: "Неизвестный город",
        stateProvince = stateProvince ?: "",
        postalCode = postalCode ?: "",
        country = country ?: "США",
        phone = phone,
        websiteUrl = websiteUrl,
        latitude = latitude,
        longitude = longitude
    )
}

fun List<BreweryDto>.toDomain(): List<Brewery> {
    return map { it.toDomain() }
}
