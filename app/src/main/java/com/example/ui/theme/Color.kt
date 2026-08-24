package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.example.data.model.ColorPalette

// Neutrals
val Neutral99 = Color(0xFFFCFDFE)
val Neutral95 = Color(0xFFF1F5F9)
val Neutral90 = Color(0xFFE2E8F0)
val Neutral80 = Color(0xFFCBD5E1)
val Neutral20 = Color(0xFF334155)
val Neutral10 = Color(0xFF0F172A)
val NeutralDarkSurface = Color(0xFF131B2E)
val NeutralDarkCard = Color(0xFF1E293B)
val NeutralAmoledBlack = Color(0xFF000000)
val NeutralAmoledCard = Color(0xFF111111)

// Emerald (Green)
val EmeraldPrimary = Color(0xFF059669)
val EmeraldSecondary = Color(0xFF10B981)
val EmeraldTertiary = Color(0xFF047857)
val EmeraldContainerLight = Color(0xFFD1FAE5)
val EmeraldContainerDark = Color(0xFF064E3B)

// Sapphire (Blue)
val SapphirePrimary = Color(0xFF1D4ED8)
val SapphireSecondary = Color(0xFF3B82F6)
val SapphireTertiary = Color(0xFF1E40AF)
val SapphireContainerLight = Color(0xFFDBEAFE)
val SapphireContainerDark = Color(0xFF1E3A8A)

// Ruby (Red)
val RubyPrimary = Color(0xFFDC2626)
val RubySecondary = Color(0xFFEF4444)
val RubyTertiary = Color(0xFFB91C1C)
val RubyContainerLight = Color(0xFFFEE2E2)
val RubyContainerDark = Color(0xFF7F1D1D)

// Amethyst (Purple)
val AmethystPrimary = Color(0xFF7C3AED)
val AmethystSecondary = Color(0xFF8B5CF6)
val AmethystTertiary = Color(0xFF6D28D9)
val AmethystContainerLight = Color(0xFFEDE9FE)
val AmethystContainerDark = Color(0xFF4C1D95)

// Amber (Gold)
val AmberPrimary = Color(0xFFD97706)
val AmberSecondary = Color(0xFFF59E0B)
val AmberTertiary = Color(0xFFB45309)
val AmberContainerLight = Color(0xFFFEF3C7)
val AmberContainerDark = Color(0xFF78350F)

// Teal (Cyan)
val TealPrimary = Color(0xFF0D9488)
val TealSecondary = Color(0xFF14B8A6)
val TealTertiary = Color(0xFF0F766E)
val TealContainerLight = Color(0xFFCCFBF1)
val TealContainerDark = Color(0xFF134E4A)

fun getCustomColorScheme(
    palette: ColorPalette,
    isDark: Boolean,
    isAmoled: Boolean = false
): ColorScheme {
    val (primary, secondary, tertiary, containerLight, containerDark) = when (palette) {
        ColorPalette.EMERALD -> listOf(EmeraldPrimary, EmeraldSecondary, EmeraldTertiary, EmeraldContainerLight, EmeraldContainerDark)
        ColorPalette.SAPPHIRE -> listOf(SapphirePrimary, SapphireSecondary, SapphireTertiary, SapphireContainerLight, SapphireContainerDark)
        ColorPalette.RUBY -> listOf(RubyPrimary, RubySecondary, RubyTertiary, RubyContainerLight, RubyContainerDark)
        ColorPalette.AMETHYST -> listOf(AmethystPrimary, AmethystSecondary, AmethystTertiary, AmethystContainerLight, AmethystContainerDark)
        ColorPalette.AMBER -> listOf(AmberPrimary, AmberSecondary, AmberTertiary, AmberContainerLight, AmberContainerDark)
        ColorPalette.TEAL -> listOf(TealPrimary, TealSecondary, TealTertiary, TealContainerLight, TealContainerDark)
    }

    return if (isDark) {
        val bg = if (isAmoled) NeutralAmoledBlack else Neutral10
        val surface = if (isAmoled) NeutralAmoledBlack else NeutralDarkSurface
        val surfaceVar = if (isAmoled) NeutralAmoledCard else NeutralDarkCard

        darkColorScheme(
            primary = secondary,
            onPrimary = Color.White,
            primaryContainer = containerDark,
            onPrimaryContainer = Color.White,
            secondary = secondary,
            onSecondary = Color.White,
            tertiary = tertiary,
            background = bg,
            onBackground = Color(0xFFF8FAFC),
            surface = surface,
            onSurface = Color(0xFFF8FAFC),
            surfaceVariant = surfaceVar,
            onSurfaceVariant = Color(0xFFCBD5E1),
            outline = Color(0xFF334155),
            outlineVariant = Color(0xFF1E293B)
        )
    } else {
        lightColorScheme(
            primary = primary,
            onPrimary = Color.White,
            primaryContainer = containerLight,
            onPrimaryContainer = primary,
            secondary = secondary,
            onSecondary = Color.White,
            tertiary = tertiary,
            background = Neutral99,
            onBackground = Neutral10,
            surface = Color.White,
            onSurface = Neutral10,
            surfaceVariant = Neutral95,
            onSurfaceVariant = Neutral20,
            outline = Neutral80,
            outlineVariant = Neutral90
        )
    }
}
