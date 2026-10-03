package com.example.android_practice.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsBar
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    object Breweries : BottomNavItem(
        title = "Пивоварни",
        icon = Icons.Default.SportsBar,
        route = Screen.List.route
    )

    object Favorites : BottomNavItem(
        title = "Избранное",
        icon = Icons.Default.Favorite,
        route = Screen.Favorites.route
    )

    object Profile : BottomNavItem(
        title = "Профиль",
        icon = Icons.Default.Person,
        route = Screen.Profile.route
    )
}
