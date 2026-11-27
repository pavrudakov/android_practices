package com.example.android_practice.feature.profile

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.android_practice.feature.profile.data.notification.ReminderNotifier
import com.example.android_practice.feature.profile.ui.ProfileScreen
import com.example.android_practice.feature.profile.ui.edit.ProfileEditScreen

/** Маршруты экранов модуля. Нужны приложению для нижней навигации и скрытия панели. */
object ProfileRoutes {
    const val PROFILE = "profile"
    const val PROFILE_EDIT = "profile_edit"
}

/** Регистрирует оба экрана профиля в графе навигации приложения. */
fun NavGraphBuilder.profileGraph(navController: NavController) {
    composable(route = ProfileRoutes.PROFILE) {
        ProfileScreen(
            onNavigateToEdit = { navController.navigate(ProfileRoutes.PROFILE_EDIT) }
        )
    }
    composable(route = ProfileRoutes.PROFILE_EDIT) {
        ProfileEditScreen(
            onBackClick = { navController.popBackStack() }
        )
    }
}

/** Точка инициализации модуля. Вызывается один раз из Application.onCreate. */
object ProfileFeature {
    fun initialize(context: Context) {
        ReminderNotifier.createNotificationChannel(context)
    }
}
