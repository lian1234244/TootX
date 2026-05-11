package com.root.toolbox

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val GeekGreen = Color(0xFF00FF41)
private val GeekGreenDark = Color(0xFF00CC33)
private val TerminalWhite = Color(0xFFF0F0F0)
private val DarkBackground = Color(0xFF000000)
private val DarkSurface = Color(0xFF0A0A0A)
private val DarkGray = Color(0xFF1A1A1A)

private val DarkColorScheme = darkColorScheme(
    primary = GeekGreen,
    onPrimary = Color.Black,
    primaryContainer = GeekGreenDark,
    onPrimaryContainer = TerminalWhite,
    secondary = GeekGreen,
    onSecondary = Color.Black,
    tertiary = GeekGreen,
    background = DarkBackground,
    onBackground = TerminalWhite,
    surface = DarkSurface,
    onSurface = TerminalWhite,
    surfaceVariant = DarkGray,
    onSurfaceVariant = GeekGreen,
    outline = GeekGreen.copy(alpha = 0.5f),
    outlineVariant = GeekGreen.copy(alpha = 0.3f)
)

@Composable
fun MainActivityTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        darkColorScheme(
            primary = GeekGreen,
            onPrimary = Color.Black,
            background = DarkBackground,
            onBackground = TerminalWhite,
            surface = DarkSurface,
            onSurface = TerminalWhite
        )
    } else {
        DarkColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            
            val windowInsetsController = WindowCompat.getInsetsController(window, view)
            windowInsetsController.isAppearanceLightStatusBars = false
            windowInsetsController.isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
