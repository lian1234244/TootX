package com.root.toolbox

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// 现代科技感配色
private val TechCyan = Color(0xFF00D4FF)
private val TechCyanDark = Color(0xFF0099CC)
private val TechGreen = Color(0xFF00FF88)
private val TerminalWhite = Color(0xFFF0F0F0)
private val DarkBackground = Color(0xFF0A0F1A)
private val DarkSurface = Color(0xFF1A1F2E)
private val DarkCard = Color(0xFF0D1117)

private val DarkColorScheme = darkColorScheme(
    primary = TechCyan,
    onPrimary = Color.Black,
    primaryContainer = TechCyanDark,
    onPrimaryContainer = TerminalWhite,
    secondary = TechCyan,
    onSecondary = Color.Black,
    tertiary = TechGreen,
    background = DarkBackground,
    onBackground = TerminalWhite,
    surface = DarkSurface,
    onSurface = TerminalWhite,
    surfaceVariant = DarkCard,
    onSurfaceVariant = TechCyan,
    outline = TechCyan.copy(alpha = 0.5f),
    outlineVariant = TechCyan.copy(alpha = 0.3f)
)

@Composable
fun MainActivityTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

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