package com.example.android_practice.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.android_practice.ui.detail.BreweryDetailScreen
import com.example.android_practice.ui.list.BreweryListScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.List.route,
        modifier = modifier
    ) {
        composable(route = Screen.List.route) {
            BreweryListScreen(
                onBreweryClick = { breweryId ->
                    navController.navigate(Screen.Detail.createRoute(breweryId))
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("breweryId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val breweryId = backStackEntry.arguments?.getString("breweryId") ?: ""
            BreweryDetailScreen(
                breweryId = breweryId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Screen.Favorites.route) {
            PlaceholderScreen(title = "Избранное")
        }

        composable(route = Screen.Profile.route) {
            PlaceholderScreen(title = "Профиль")
        }
    }
}
