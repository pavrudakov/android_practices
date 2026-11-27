package com.example.android_practice.ui.navigation

import com.example.android_practice.feature.profile.ProfileRoutes

sealed class Screen(val route: String) {
    object List : Screen("breweries")
    object Detail : Screen("brewery_detail/{breweryId}") {
        fun createRoute(breweryId: String): String = "brewery_detail/$breweryId"
    }
    object Settings : Screen("settings")
    object Favorites : Screen("favorites")
    object Profile : Screen(ProfileRoutes.PROFILE)
    object ProfileEdit : Screen(ProfileRoutes.PROFILE_EDIT)
}
