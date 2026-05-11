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
import com.beepstop.ui.theme.F1TeamColors
import com.beepstop.ui.theme.toComposeColor
import com.beepstop.widget.model.StandingsWidgetItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first

class StandingsWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val prefs = PreferencesManager(context)
        val bgHex      = prefs.observe(PreferencesManager.KEY_BG_COLOR,            "1C1C1E").first()
        val fontHex    = prefs.observe(PreferencesManager.KEY_FONT_COLOR,          "FFFFFF").first()
        val mode       = prefs.observe(PreferencesManager.KEY_STANDINGS_SMALL_MODE, "team").first()
        val cachedJson = prefs.observe(PreferencesManager.KEY_STANDINGS_CACHE,      "[]").first()

        val bgColor   = bgHex.toComposeColor()
        val fontColor = fontHex.toComposeColor()

        val allItems: List<StandingsWidgetItem> = try {
            val type = object : TypeToken<List<StandingsWidgetItem>>() {}.type
            Gson().fromJson(cachedJson, type) ?: emptyList()
        } catch (e: Exception) { emptyList() }

        val items = allItems.filter { it.mode == mode }.take(3)

        provideContent {
            GlanceTheme {
                Column(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(bgColor)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (items.isEmpty()) {
                        Text(
                            text = "No data",
                            style = TextStyle(color = ColorProvider(fontColor), fontSize = 11.sp)
                        )
                    } else {
                        items.forEachIndexed { index, item ->
                            val teamColor = F1TeamColors.forConstructor(item.constructorId)
                            val logo = ImageCacheHelper.getCachedLogo(context, item.constructorId)
                            Row(
                                modifier = GlanceModifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (logo != null) {
                                    Image(
                                        provider = ImageProvider(logo),
                                        contentDescription = item.name,
                                        modifier = GlanceModifier.size(16.dp)
                                    )
                                    Spacer(GlanceModifier.width(5.dp))
                                }
                                Text(
                                    text = item.name,
                                    style = TextStyle(
                                        color = ColorProvider(fontColor),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    maxLines = 1,
                                    modifier = GlanceModifier.defaultWeight()
                                )
                                Text(
                                    text = item.points,
                                    style = TextStyle(
                                        color = ColorProvider(teamColor),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            if (index < items.lastIndex) Spacer(GlanceModifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}

class StandingsWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = StandingsWidget()
}
