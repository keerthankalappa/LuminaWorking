package com.example.lumina.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val LightColorScheme = lightColorScheme(
    primary = LuminaPrimary,
    onPrimary = LuminaOnPrimary,
    primaryContainer = LuminaPrimaryContainer,
    onPrimaryContainer = LuminaOnPrimaryContainer,
    secondary = LuminaSecondary,
    onSecondary = LuminaOnSecondary,
    secondaryContainer = LuminaSecondaryContainer,
    onSecondaryContainer = LuminaOnSecondaryContainer,
    tertiary = LuminaTertiary,
    onTertiary = LuminaOnPrimary,
    tertiaryContainer = LuminaTertiaryContainer,
    onTertiaryContainer = LuminaOnSurface,
    background = LuminaSurface,
    onBackground = LuminaOnSurface,
    surface = LuminaSurface,
    onSurface = LuminaOnSurface,
    surfaceVariant = LuminaSurfaceContainerLow,
    onSurfaceVariant = LuminaOnSurfaceVariant,
    outline = LuminaOutline,
    outlineVariant = LuminaOutlineVariant,
    error = LuminaError,
    errorContainer = LuminaErrorContainer
)

private val DarkColorScheme = darkColorScheme(
    primary = LuminaPrimaryFixedDim,
    onPrimary = LuminaOnPrimaryFixed,
    primaryContainer = LuminaPrimary,
    onPrimaryContainer = LuminaPrimaryFixed,
    secondary = LuminaSecondaryFixedDim,
    onSecondary = LuminaOnSurface,
    secondaryContainer = LuminaSecondary,
    onSecondaryContainer = LuminaSecondaryFixed,
    background = LuminaInverseSurface,
    surface = LuminaInverseSurface,
    onSurface = LuminaCream,
    onSurfaceVariant = LuminaOutlineVariant
)

val LuminaTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.3).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp
    )
)

@Composable
fun LuminaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = LuminaTypography,
        content = content
    )
}
