package com.syncup.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1DB584),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF0E8C63),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF1DB584),
    onSecondary = Color.White,
    tertiary = Color(0xFF4DD4AC),
    onTertiary = Color.White,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    error = Color(0xFFCF6679),
    onError = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1DB584),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8F5F3),
    onPrimaryContainer = Color(0xFF0E5C41),
    secondary = Color(0xFF1DB584),
    onSecondary = Color.White,
    tertiary = Color(0xFF4DD4AC),
    onTertiary = Color.White,
    background = Color(0xFFF8F9FA),
    onBackground = Color(0xFF1A1A1A),
    surface = Color.White,
    onSurface = Color(0xFF1A1A1A),
    error = Color(0xFFB3261E),
    onError = Color.White
)

@Composable
fun SyncUpTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = SyncUpTypography,
        content = content
    )
}
