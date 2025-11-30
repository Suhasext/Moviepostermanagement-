package com.example.moviepostermanagementapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.moviepostermanagementapp.ui.theme.PrimaryPurple
import com.example.moviepostermanagementapp.ui.theme.SecondaryPink
import com.example.moviepostermanagementapp.ui.theme.BackgroundDark
import com.example.moviepostermanagementapp.ui.theme.TextPrimary
import com.example.moviepostermanagementapp.ui.theme.TextSecondary
import com.example.moviepostermanagementapp.ui.theme.BorderColor

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryPurple,
    secondary = SecondaryPink,
    tertiary = CineVaultDarkTertiary,
    background = BackgroundDark,
    surface = Color(0xFF1E1E2E),
    surfaceVariant = Color(0xFF2A2A2E),
    onPrimary = TextPrimary,
    onSecondary = TextPrimary,
    onTertiary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    primaryContainer = PrimaryPurple,
    onPrimaryContainer = TextPrimary,
    secondaryContainer = SecondaryPink,
    onSecondaryContainer = TextPrimary,
    tertiaryContainer = CineVaultDarkTertiary,
    onTertiaryContainer = TextPrimary,
    error = Color(0xFFCF6679),
    onError = Color(0xFF000000),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    outline = BorderColor,
    outlineVariant = Color(0xFF2A2A2E),
    scrim = Color(0xFF000000),
    inverseSurface = TextSecondary,
    inverseOnSurface = BackgroundDark,
    inversePrimary = CineVaultDarkTertiary
)

private val LightColorScheme = lightColorScheme(
    primary = CineVaultPrimary,
    secondary = CineVaultSecondary,
    tertiary = CineVaultTertiary,
    background = CineVaultBackground,
    surface = CineVaultSurface,
    surfaceVariant = CineVaultSurfaceVariant,
    onPrimary = CineVaultOnPrimary,
    onSecondary = CineVaultOnPrimary,
    onTertiary = CineVaultOnPrimary,
    onBackground = CineVaultOnBackground,
    onSurface = CineVaultOnSurface,
    onSurfaceVariant = CineVaultOnBackground.copy(alpha = 0.7f),
    primaryContainer = CineVaultSecondary,
    onPrimaryContainer = CineVaultOnPrimary,
    secondaryContainer = CineVaultTertiary,
    onSecondaryContainer = CineVaultOnPrimary,
    tertiaryContainer = CineVaultTertiary,
    onTertiaryContainer = CineVaultOnPrimary,
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    outline = CineVaultSecondary.copy(alpha = 0.5f),
    outlineVariant = CineVaultSurfaceVariant,
    scrim = Color(0xFF000000),
    inverseSurface = CineVaultOnBackground,
    inverseOnSurface = CineVaultBackground,
    inversePrimary = CineVaultTertiary
)

@Composable
fun MoviePosterManagementAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled for consistent black & purple branding
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
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