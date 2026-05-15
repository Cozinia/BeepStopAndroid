package com.beepstop.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class TyreType(val color: Color, val label: String, val maxValue: Int) {
    SOFT(Color(0xFFFF1E1E), "D", 31),
    MEDIUM(Color(0xFFFFC906), "H", 24),
    HARD(Color(0xFFFFFFFF), "M", 60),
    ULTRASOFT(Color(0xFFCC00FF), "MO", 12),
}

@Composable
fun TyreRing(
    value: Int,
    tyre: TyreType,
    fontColor: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = modifier.size(60.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(60.dp)) {
                val strokeWidth = 6.dp.toPx()
                val inset = strokeWidth / 2 + 2f
                val arcSize = Size(size.width - inset * 2, size.height - inset * 2)
                val topLeft = Offset(inset, inset)

                drawArc(
                    color = Color.White.copy(alpha = 0.2f),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                val sweep = (value.toFloat() / tyre.maxValue.coerceAtLeast(1)) * 360f
                if (sweep > 0f) {
                    drawArc(
                        color = tyre.color,
                        startAngle = -90f,
                        sweepAngle = sweep,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }
            }
            Text(
                text = value.toString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = fontColor
            )
        }
        Text(
            text = tyre.label,
            fontSize = 9.sp,
            color = fontColor.copy(alpha = 0.7f)
        )
    }
}
