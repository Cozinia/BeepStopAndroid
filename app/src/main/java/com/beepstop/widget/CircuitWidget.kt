package com.beepstop.widget

import android.content.Context
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentHeight
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.beepstop.data.local.PreferencesManager
import com.beepstop.ui.theme.toComposeColor
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class CircuitWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val prefs = PreferencesManager(context)
        val bgHex            = prefs.observe(PreferencesManager.KEY_BG_COLOR,           "1C1C1E").first()
        val fontHex          = prefs.observe(PreferencesManager.KEY_FONT_COLOR,         "FFFFFF").first()
        val circuitHex       = prefs.observe(PreferencesManager.KEY_CIRCUIT_COLOR,      "DC0000").first()
        val nextSessionDate  = prefs.observe(PreferencesManager.KEY_NEXT_SESSION_DATE,  0L).first()
        val nextSessionRace  = prefs.observe(PreferencesManager.KEY_NEXT_SESSION_RACE,  "").first()
        val nextSessionLabel = prefs.observe(PreferencesManager.KEY_NEXT_SESSION_LABEL, "").first()
        val nextCircuitId    = prefs.observe(PreferencesManager.KEY_NEXT_CIRCUIT_ID,    "").first()
        val nextCircuitName  = prefs.observe(PreferencesManager.KEY_NEXT_CIRCUIT_NAME,  "").first()

        val bgColor     = bgHex.toComposeColor()
        val fontColor   = fontHex.toComposeColor()
        val accentColor = circuitHex.toComposeColor()

        val dateText = if (nextSessionDate > 0) {
            Instant.ofEpochMilli(nextSessionDate)
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("d MMM  HH:mm"))
        } else ""

        // Render at widget height (1 cell ≈ 74dp → 148px @2×)
        val mapBitmap = if (nextCircuitId.isNotBlank()) {
            CircuitRenderer.render(context, nextCircuitId, 148, 148, accentColor)
        } else null

        provideContent {
            GlanceTheme {
                Row(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(bgColor)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Map: square, fills the full widget height
                    if (mapBitmap != null) {
                        Image(
                            provider = ImageProvider(mapBitmap),
                            contentDescription = "Circuit map",
                            contentScale = ContentScale.Fit,
                            modifier = GlanceModifier
                                .fillMaxHeight()
                                .width(58.dp)
                        )
                        Spacer(GlanceModifier.width(10.dp))
                    }

                    // Text: takes all remaining width, centered vertically
                    Column(
                        modifier = GlanceModifier
                            .defaultWeight()
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (nextSessionRace.isBlank()) {
                            Text(
                                text = "No upcoming race",
                                style = TextStyle(color = ColorProvider(fontColor), fontSize = 12.sp)
                            )
                        } else {
                            Text(
                                text = nextSessionRace,
                                style = TextStyle(
                                    color = ColorProvider(fontColor),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                ),
                                maxLines = 1
                            )
                            if (nextCircuitName.isNotBlank()) {
                                Text(
                                    text = nextCircuitName,
                                    style = TextStyle(
                                        color = ColorProvider(fontColor.copy(alpha = 0.65f)),
                                        fontSize = 11.sp
                                    ),
                                    maxLines = 1
                                )
                            }
                            if (dateText.isNotBlank()) {
                                Text(
                                    text = dateText,
                                    style = TextStyle(
                                        color = ColorProvider(fontColor.copy(alpha = 0.8f)),
                                        fontSize = 11.sp
                                    ),
                                    maxLines = 1
                                )
                            }
                            if (nextSessionLabel.isNotBlank()) {
                                Spacer(GlanceModifier.height(3.dp))
                                Text(
                                    text = nextSessionLabel.uppercase(),
                                    style = TextStyle(
                                        color = ColorProvider(accentColor),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

class CircuitWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = CircuitWidget()
}
