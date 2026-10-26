package com.example.android_practice.data.mapper

import com.example.android_practice.data.local.entity.FavoriteBreweryEntity
import com.example.android_practice.domain.model.Brewery

fun FavoriteBreweryEntity.toDomain(): Brewery {
    return Brewery(
        id = id,
        name = name,
        breweryType = breweryType,
        street = street,
        city = city,
        stateProvince = stateProvince,
        postalCode = postalCode,
        country = country,
        phone = phone,
        websiteUrl = websiteUrl,
        latitude = latitude,
        longitude = longitude
    )
}

fun Brewery.toEntity(): FavoriteBreweryEntity {
    return FavoriteBreweryEntity(
        id = id,
        name = name,
        breweryType = breweryType,
        street = street,
        city = city,
        stateProvince = stateProvince,
        postalCode = postalCode,
        country = country,
        phone = phone,
        websiteUrl = websiteUrl,
        latitude = latitude,
        longitude = longitude
    )
}
