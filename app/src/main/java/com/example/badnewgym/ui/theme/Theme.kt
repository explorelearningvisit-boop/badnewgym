package com.example.badnewgym.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/**
 * BAD GYM uses a light-only visual system.
 *
 * The app must not switch into a dark/black UI because of Android system
 * appearance or dynamic-colour settings. Brand and semantic colours are
 * deliberately explicit so screenshots and production UI remain consistent.
 */
private val BadGymLightColorScheme = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF047857),
    onPrimary = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    primaryContainer = androidx.compose.ui.graphics.Color(0xFFD1FAE5),
    onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFF064E3B),
    secondary = androidx.compose.ui.graphics.Color(0xFF0F766E),
    onSecondary = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    secondaryContainer = androidx.compose.ui.graphics.Color(0xFFCCFBF1),
    onSecondaryContainer = androidx.compose.ui.graphics.Color(0xFF134E4A),
    tertiary = androidx.compose.ui.graphics.Color(0xFF2563EB),
    onTertiary = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    background = androidx.compose.ui.graphics.Color(0xFFF7FBF8),
    onBackground = androidx.compose.ui.graphics.Color(0xFF173B2F),
    surface = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    onSurface = androidx.compose.ui.graphics.Color(0xFF173B2F),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFFEAF5EF),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFF49665A),
    outline = androidx.compose.ui.graphics.Color(0xFFB8CEC2),
    outlineVariant = androidx.compose.ui.graphics.Color(0xFFDCE9E1),
    error = androidx.compose.ui.graphics.Color(0xFFB42318),
    onError = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    errorContainer = androidx.compose.ui.graphics.Color(0xFFFEE4E2),
    onErrorContainer = androidx.compose.ui.graphics.Color(0xFF7A271A),
)

/**
 * Kept source-compatible with existing callers. The parameters are intentionally
 * ignored: BAD GYM does not opt into system dark mode or Android dynamic colours.
 */
@Suppress("UNUSED_PARAMETER")
@Composable
fun BadNewgymTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = BadGymLightColorScheme,
        typography = Typography,
        content = content,
    )
}
