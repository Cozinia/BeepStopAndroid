package com.beepstop.ui.theme

import androidx.compose.ui.graphics.Color

object F1TeamColors {
    val map: Map<String, Color> = mapOf(
        "ferrari"       to Color(0xFFDC0000),
        "mclaren"       to Color(0xFFFF8000),
        "red_bull"      to Color(0xFF3671C6),
        "mercedes"      to Color(0xFF27F4D2),
        "aston_martin"  to Color(0xFF358C75),
        "alpine"        to Color(0xFF0093CC),
        "williams"      to Color(0xFF37BEDD),
        "haas"          to Color(0xFFB6BABD),
        "sauber"        to Color(0xFF52E252),
        "rb"            to Color(0xFF6692FF),
        "cadillac"      to Color(0xFFFFFFFF),
    )

    val hexMap: Map<String, String> = mapOf(
        "ferrari"       to "DC0000",
        "mclaren"       to "FF8000",
        "red_bull"      to "3671C6",
        "mercedes"      to "27F4D2",
        "aston_martin"  to "358C75",
        "alpine"        to "0093CC",
        "williams"      to "37BEDD",
        "haas"          to "B6BABD",
        "sauber"        to "52E252",
        "rb"            to "6692FF",
        "cadillac"      to "FFFFFF",
    )

    val pickerSwatches: List<Pair<String, String>> = listOf(
        "Ferrari"       to "DC0000",
        "McLaren"       to "FF8000",
        "Red Bull"      to "3671C6",
        "Mercedes"      to "27F4D2",
        "Aston Martin"  to "358C75",
        "Alpine"        to "0093CC",
        "Williams"      to "37BEDD",
        "Haas"          to "B6BABD",
        "Audi"          to "52E252",
        "RB"            to "6692FF",
        "Cadillac"      to "FFFFFF",
        "Dark"          to "1C1C1E",
        "White"         to "FFFFFF",
    )

    fun forConstructor(id: String): Color = map[id] ?: Color(0xFF1C1C1E)
}

fun Color.toHex(): String {
    val r = (red * 255).toInt()
    val g = (green * 255).toInt()
    val b = (blue * 255).toInt()
    return "%02X%02X%02X".format(r, g, b)
}

fun String.toComposeColor(): Color {
    val hex = removePrefix("#").removePrefix("0x").removePrefix("0X")
    return try {
        Color(android.graphics.Color.parseColor("#$hex"))
    } catch (e: Exception) {
        Color(0xFF1C1C1E)
    }
}
