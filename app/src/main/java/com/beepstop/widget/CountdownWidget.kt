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
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.beepstop.data.local.PreferencesManager
import com.beepstop.ui.theme.toComposeColor
import kotlinx.coroutines.flow.first
import java.time.Duration
import java.time.Instant

class CountdownWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val prefs = PreferencesManager(context)
        val bgHex            = prefs.observe(PreferencesManager.KEY_COUNTDOWN_BG_COLOR,  "1C1C1E").first()
        val fontHex          = prefs.observe(PreferencesManager.KEY_COUNTDOWN_FONT_COLOR, "FFFFFF").first()
        val nextSessionDate  = prefs.observe(PreferencesManager.KEY_NEXT_SESSION_DATE,    0L).first()
        val nextSessionLabel = prefs.observe(PreferencesManager.KEY_NEXT_SESSION_LABEL,   "").first()
        val nextSessionRace  = prefs.observe(PreferencesManager.KEY_NEXT_SESSION_RACE,    "").first()

        val bgColor   = bgHex.toComposeColor()
        val fontColor = fontHex.toComposeColor()

        val now          = Instant.now()
        val sessionStart = if (nextSessionDate > 0) Instant.ofEpochMilli(nextSessionDate) else null
        val isLive       = sessionStart != null && now >= sessionStart && now < sessionStart.plusSeconds(7200)
        val isUpcoming   = sessionStart != null && now < sessionStart

        val days: Int; val hours: Int; val minutes: Int
        if (isUpcoming && sessionStart != null) {
            val d = Duration.between(now, sessionStart)
            days    = d.toDays().toInt().coerceIn(0, 31)
            hours   = (d.toHours() % 24).toInt().coerceIn(0, 23)
            minutes = (d.toMinutes() % 60).toInt().coerceIn(0, 59)
        } else {
            days = 0; hours = 0; minutes = 0
        }

        // Ring size tuned to fit 2 side-by-side inside a 2×2 widget (~110dp wide)
        // Two rings per row: (110 - 2*padding - spacer) / 2 ≈ 44dp each
        val ringPx = 88  // 44dp @ 2× density
        val softBitmap = TyreRingRenderer.render(days,    WidgetTyreType.SOFT,   fontColor, ringPx)
        val medBitmap  = TyreRingRenderer.render(hours,   WidgetTyreType.MEDIUM, fontColor, ringPx)
        val hardBitmap = TyreRingRenderer.render(minutes, WidgetTyreType.HARD,   fontColor, ringPx)

        provideContent {
            GlanceTheme {
                Column(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(bgColor)
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when {
                        nextSessionDate == 0L || sessionStart == null -> {
                            Text(
                                text = "No upcoming\nsession",
                                style = TextStyle(
                                    color = ColorProvider(fontColor),
                                    fontSize = 12.sp
                                )
                            )
                        }
                        isLive -> {
                            Text(
                                text = "RACE LIVE",
                                style = TextStyle(
                                    color = ColorProvider(fontColor),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            )
                            Spacer(GlanceModifier.height(4.dp))
                            Text(
                                text = nextSessionRace,
                                style = TextStyle(color = ColorProvider(fontColor), fontSize = 11.sp),
                                maxLines = 2
                            )
                        }
                        isUpcoming -> {
                            // Row 1: Days + Hours
                            Row(
                                modifier = GlanceModifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    provider = ImageProvider(softBitmap),
                                    contentDescription = "$days days",
                                    modifier = GlanceModifier.size(44.dp)
                                )
                                Spacer(GlanceModifier.width(6.dp))
                                Image(
                                    provider = ImageProvider(medBitmap),
                                    contentDescription = "$hours hours",
                                    modifier = GlanceModifier.size(44.dp)
                                )
                            }
                            Spacer(GlanceModifier.height(4.dp))
                            // Row 2: Minutes (centered)
                            Row(
                                modifier = GlanceModifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    provider = ImageProvider(hardBitmap),
                                    contentDescription = "$minutes minutes",
                                    modifier = GlanceModifier.size(44.dp)
                                )
                            }
                            Spacer(GlanceModifier.height(4.dp))
                            // Label + race name
                            if (nextSessionLabel.isNotBlank()) {
                                Text(
                                    text = nextSessionLabel.uppercase(),
                                    style = TextStyle(
                                        color = ColorProvider(fontColor),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                            if (nextSessionRace.isNotBlank()) {
                                Text(
                                    text = nextSessionRace,
                                    style = TextStyle(
                                        color = ColorProvider(fontColor.copy(alpha = 0.7f)),
                                        fontSize = 10.sp
                                    ),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

class CountdownWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = CountdownWidget()
}
