package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val MedicalDarkColorScheme = darkColorScheme(
    primary = MedicalPrimaryLight,
    onPrimary = Color.White,
    primaryContainer = MedicalPrimaryDark,
    onPrimaryContainer = Color.White,
    secondary = MedicalGreenLight,
    onSecondary = Color.Black,
    secondaryContainer = MedicalGreenDark,
    onSecondaryContainer = Color.White,
    tertiary = Spo2Cyan,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    error = MedicalRedLight,
    onError = Color.White
)

private val MedicalLightColorScheme = lightColorScheme(
    primary = MedicalPrimary,
    onPrimary = Color.White,
    primaryContainer = MedicalPrimarySoft,
    onPrimaryContainer = MedicalPrimaryDark,
    secondary = MedicalGreen,
    onSecondary = Color.White,
    tertiary = Spo2Cyan,
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1),
    error = MedicalRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to clinical dark mode for high-contrast ER monitors
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) MedicalDarkColorScheme else MedicalLightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DarkBackground.toArgb()
            window.navigationBarColor = DarkBackground.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
