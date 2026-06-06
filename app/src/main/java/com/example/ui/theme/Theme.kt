package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NepalCrimsonRegal,
    secondary = NepalNavySlate,
    tertiary = AccentGold,
    background = SlateDuskBg,
    surface = SlateCardBg,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = SlateDuskBg,
    onBackground = TextPrimaryDusk,
    onSurface = TextPrimaryDusk
)

private val LightColorScheme = lightColorScheme(
    primary = SleekPrimary,
    secondary = SleekSecondary,
    tertiary = SleekTertiary,
    background = LightBackground,
    surface = LightSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = SleekOnTertiary,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    outline = BorderLight,
    primaryContainer = SleekSearchBg,
    onPrimaryContainer = TextSecondaryLight,
    surfaceVariant = SleekSearchBg,
    onSurfaceVariant = TextSecondaryLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isHighContrast: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        isHighContrast -> {
            // High-Contrast mode for accessibility
            darkColorScheme(
                primary = Color.Yellow,
                secondary = Color.White,
                tertiary = Color.Cyan,
                background = Color.Black,
                surface = Color(0xFF1C1E21),
                onPrimary = Color.Black,
                onSecondary = Color.Black,
                onTertiary = Color.Black,
                onBackground = Color.White,
                onSurface = Color.White
            )
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
