package com.beepstop.widget

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

enum class WidgetTyreType(val color: Color, val label: String, val maxValue: Int) {
    SOFT(Color(0xFFFF1E1E), "D", 31),
    MEDIUM(Color(0xFFFFC906), "H", 24),
    HARD(Color(0xFFFFFFFF), "M", 60),
    ULTRASOFT(Color(0xFFCC00FF), "MO", 12),
}

object TyreRingRenderer {
    fun render(
        value: Int,
        tyre: WidgetTyreType,
        fontColor: Color,
        sizePx: Int = 120
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val strokeWidth = sizePx * 0.08f
        val padding = strokeWidth / 2 + 4f
        val oval = RectF(padding, padding, sizePx - padding, sizePx - padding)

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            this.strokeWidth = strokeWidth
            strokeCap = Paint.Cap.ROUND
            color = android.graphics.Color.argb(51, 255, 255, 255) // 20% white
        }
        canvas.drawArc(oval, -90f, 360f, false, bgPaint)

        val sweep = (value.toFloat() / tyre.maxValue.coerceAtLeast(1)) * 360f
        val fgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            this.strokeWidth = strokeWidth
            strokeCap = Paint.Cap.ROUND
            color = tyre.color.toArgb()
        }
        if (sweep > 0f) canvas.drawArc(oval, -90f, sweep, false, fgPaint)

        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = fontColor.toArgb()
            textSize = sizePx * 0.28f
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
        }
        val cx = sizePx / 2f
        val cy = sizePx / 2f - (textPaint.descent() + textPaint.ascent()) / 2
        canvas.drawText(value.toString(), cx, cy, textPaint)

        val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = fontColor.copy(alpha = 0.7f).toArgb()
            textSize = sizePx * 0.16f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(tyre.label, cx, sizePx * 0.82f, labelPaint)

        return bitmap
    }
}
