package com.grameenlight.app.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NightColors = darkColorScheme(
    primary        = Color(0xFFF59E0B),
    onPrimary      = Color(0xFF1A1A1A),
    background     = Color(0xFF0D1B2A),
    surface        = Color(0xFF1A2A3A),
    onBackground   = Color(0xFFE8F4FF),
    onSurface      = Color(0xFFE8F4FF),
    secondary      = Color(0xFF7FA8C8),
    onSecondary    = Color(0xFF0D1B2A)
)

private val DayColors = lightColorScheme(
    primary        = Color(0xFF1565C0),
    onPrimary      = Color(0xFFFFFFFF),
    background     = Color(0xFFF0F4F8),
    surface        = Color(0xFFFFFFFF),
    onBackground   = Color(0xFF1A202C),
    onSurface      = Color(0xFF1A202C),
    secondary      = Color(0xFF718096),
    onSecondary    = Color(0xFFFFFFFF)
)

@Composable
fun GrameenLightTheme(darkTheme: Boolean = true, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) NightColors else DayColors,
        content = content
    )
}
