package com.beepstop.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LightingColorFilter
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.LruCache
import com.beepstop.R
import com.caverock.androidsvg.SVG

private val cache = LruCache<String, Bitmap>(20)

private val circuitRawMap: Map<String, Int> = mapOf(
    "bahrain" to R.raw.bahrain, "bahrain_outer" to R.raw.bahrain,
    "jeddah" to R.raw.jeddah,
    "albert_park" to R.raw.australia,
    "suzuka" to R.raw.japan,
    "shanghai" to R.raw.china,
    "miami" to R.raw.miami,
    "imola" to R.raw.italy, "monza" to R.raw.italy,
    "monaco" to R.raw.monaco,
    "villeneuve" to R.raw.canada,
    "catalunya" to R.raw.spain, "madrid" to R.raw.madring,
    "red_bull_ring" to R.raw.austria,
    "silverstone" to R.raw.greatbritain,
    "hungaroring" to R.raw.hungary,
    "spa" to R.raw.belgium,
    "zandvoort" to R.raw.netherlands,
    "baku" to R.raw.azerbaijan,
    "marina_bay" to R.raw.singapore,
    "americas" to R.raw.americas,
    "rodriguez" to R.raw.mexico,
    "interlagos" to R.raw.brazil,
    "vegas" to R.raw.vegas,
    "losail" to R.raw.losail,
    "yas_marina" to R.raw.usa,
    "hanoi" to R.raw.vietnam,
    "sochi" to R.raw.russia,
    "ricard" to R.raw.france,
)

object CircuitRenderer {

    /**
     * Renders the circuit SVG as a [Bitmap] tinted in [tintColor].
     * Results are cached by "$circuitId-$tintColor".
     */
    fun render(
        context: Context,
        circuitId: String,
        widthPx: Int,
        heightPx: Int,
        tintColor: androidx.compose.ui.graphics.Color
    ): Bitmap? {
        val rawRes = circuitRawMap[circuitId] ?: return null
        val argb = tintColor.toArgb()
        val cacheKey = "$circuitId-${argb}-${widthPx}x${heightPx}"
        cache.get(cacheKey)?.let { return it }

        return try {
            // Load and recolour the SVG: replace the hardcoded track stroke colour with tintColor
            val svgText = context.resources.openRawResource(rawRes)
                .bufferedReader().use { it.readText() }
            val tintHex = "#%06X".format(argb and 0xFFFFFF)
            val recoloured = svgText.replace(
                Regex("""stroke:#[0-9A-Fa-f]{6}"""),
                "stroke:$tintHex"
            )

            val svg = SVG.getFromString(recoloured)
            svg.setDocumentWidth(widthPx.toFloat())
            svg.setDocumentHeight(heightPx.toFloat())

            val bitmap = Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888)
            svg.renderToCanvas(Canvas(bitmap))
            cache.put(cacheKey, bitmap)
            bitmap
        } catch (e: Exception) {
            null
        }
    }

    fun clearCache() = cache.evictAll()
}

private fun androidx.compose.ui.graphics.Color.toArgb(): Int =
    android.graphics.Color.argb(
        (alpha * 255).toInt(),
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt()
    )
