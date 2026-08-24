package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.data.model.ColorPalette
import com.example.data.model.DarkModeOption

@Composable
fun NewsPulseTheme(
    palette: ColorPalette = ColorPalette.EMERALD,
    darkModeOption: DarkModeOption = DarkModeOption.SYSTEM,
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val isDark = when (darkModeOption) {
        DarkModeOption.SYSTEM -> systemDark
        DarkModeOption.LIGHT -> false
        DarkModeOption.DARK, DarkModeOption.AMOLED -> true
    }
    val isAmoled = darkModeOption == DarkModeOption.AMOLED

    val colorScheme = getCustomColorScheme(
        palette = palette,
        isDark = isDark,
        isAmoled = isAmoled
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
