package com.example.android_practice.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = AmberAle,
    onPrimary = Color.White,
    primaryContainer = DarkMalt,
    onPrimaryContainer = WarmCream,
    secondary = HopGreen,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8F5E9),
    onSecondaryContainer = Color(0xFF2E7D32),
    background = WarmCream,
    onBackground = DarkRoast,
    surface = WarmSurface,
    onSurface = DarkRoast,
    surfaceVariant = SurfaceVariantWarm,
    onSurfaceVariant = MutedBrown,
    outline = LightBeige,
    error = Terracotta,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = AmberAle,
    onPrimary = DarkRoast,
    primaryContainer = DarkMalt,
    onPrimaryContainer = WarmCream,
    secondary = HopGreen,
    onSecondary = Color.White,
    background = DarkOakBackground,
    onBackground = WarmCream,
    surface = DarkOakSurface,
    onSurface = WarmCream,
    surfaceVariant = DarkOakSurfaceVariant,
    onSurfaceVariant = DarkMutedBrown,
    outline = DarkOutline,
    error = Terracotta,
    onError = Color.White
)

@Composable
fun Android_practiceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CraftTypography,
        content = content
    )
}
