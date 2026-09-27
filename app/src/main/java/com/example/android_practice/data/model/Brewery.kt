package com.example.android_practice.data.model

data class Brewery(
    val id: String,
    val name: String,
    val breweryType: String,
    val street: String?,
    val city: String,
    val stateProvince: String,
    val postalCode: String,
    val country: String,
    val phone: String?,
    val websiteUrl: String?,
    val latitude: Double?,
    val longitude: Double?
)
