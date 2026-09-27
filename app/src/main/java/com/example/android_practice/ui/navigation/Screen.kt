package com.example.android_practice.ui.navigation

sealed class Screen(val route: String) {
    object List : Screen("breweries")
    object Detail : Screen("brewery_detail/{breweryId}") {
        fun createRoute(breweryId: String): String = "brewery_detail/$breweryId"
    }
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
}
