package edu.alisson.anota.presentation.utils

import androidx.compose.ui.graphics.Color

/**
 * Converts a [Color] to a hex string (e.g., "#FF5733").
 */
fun Color.toHex(): String {
    val red = (red * 255).toInt()
    val green = (green * 255).toInt()
    val blue = (blue * 255).toInt()
    return String.format("#%02X%02X%02X", red, green, blue)
}

/**
 * Converts a hex string (e.g., "#FF5733") to a [Color].
 */
fun hexToColor(hex: String): Color {
    return Color(android.graphics.Color.parseColor(hex))
}

