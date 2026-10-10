package com.example.android_practice.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BreweryDto(
    val id: String? = null,
    val name: String? = null,
    @SerialName("brewery_type")
    val breweryType: String? = null,
    val street: String? = null,
    val city: String? = null,
    @SerialName("state_province")
    val stateProvince: String? = null,
    @SerialName("postal_code")
    val postalCode: String? = null,
    val country: String? = null,
    val phone: String? = null,
    @SerialName("website_url")
    val websiteUrl: String? = null,
    @Serializable(with = FlexibleDoubleSerializer::class)
    val latitude: Double? = null,
    @Serializable(with = FlexibleDoubleSerializer::class)
    val longitude: Double? = null
)
