package com.example.android_practice.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_breweries")
data class FavoriteBreweryEntity(
    @PrimaryKey val id: String,
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
