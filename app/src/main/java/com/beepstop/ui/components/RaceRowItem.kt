package com.beepstop.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.beepstop.data.model.Race
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val flagMap = mapOf(
    // Bahrain
    "bahrain" to "🇧🇭",
    "bahrain_outer" to "🇧🇭",
    // Saudi Arabia
    "jeddah" to "🇸🇦",
    // Australia
    "albert_park" to "🇦🇺",
    "adelaide" to "🇦🇺",
    // Japan
    "suzuka" to "🇯🇵",
    "fuji" to "🇯🇵",
    // China
    "shanghai" to "🇨🇳",
    // United States
    "miami" to "🇺🇸",
    "americas" to "🇺🇸",
    "vegas" to "🇺🇸",
    "indianapolis" to "🇺🇸",
    "watkins_glen" to "🇺🇸",
    "long_beach" to "🇺🇸",
    "caesars_palace" to "🇺🇸",
    "detroit" to "🇺🇸",
    "dallas" to "🇺🇸",
    "phoenix" to "🇺🇸",
    "sebring" to "🇺🇸",
    "riverside" to "🇺🇸",
    // Italy
    "imola" to "🇮🇹",
    "monza" to "🇮🇹",
    "mugello" to "🇮🇹",
    "pescara" to "🇮🇹",
    // Monaco
    "monaco" to "🇲🇨",
    // Canada
    "villeneuve" to "🇨🇦",
    // Spain
    "catalunya" to "🇪🇸",
    "jerez" to "🇪🇸",
    "valencia" to "🇪🇸",
    "pedralbes" to "🇪🇸",
    "madrid" to "🇪🇸",
    // Austria
    "red_bull_ring" to "🇦🇹",
    "zeltweg" to "🇦🇹",
    // Great Britain
    "silverstone" to "🇬🇧",
    "brands_hatch" to "🇬🇧",
    "aintree" to "🇬🇧",
    "donington" to "🇬🇧",
    // Hungary
    "hungaroring" to "🇭🇺",
    // Belgium
    "spa" to "🇧🇪",
    "nivelles" to "🇧🇪",
    // Netherlands
    "zandvoort" to "🇳🇱",
    // Azerbaijan
    "baku" to "🇦🇿",
    // Singapore
    "marina_bay" to "🇸🇬",
    // Mexico
    "rodriguez" to "🇲🇽",
    // Brazil
    "interlagos" to "🇧🇷",
    "jacarepagua" to "🇧🇷",
    // UAE
    "yas_marina" to "🇦🇪",
    // Qatar
    "losail" to "🇶🇦",
    // Portugal
    "portimao" to "🇵🇹",
    "estoril" to "🇵🇹",
    "boavista" to "🇵🇹",
    "monsanto" to "🇵🇹",
    // Turkey
    "istanbul" to "🇹🇷",
    // Germany
    "nurburgring" to "🇩🇪",
    "hockenheimring" to "🇩🇪",
    "avus" to "🇩🇪",
    // France
    "ricard" to "🇫🇷",
    "magny_cours" to "🇫🇷",
    "dijon" to "🇫🇷",
    "reims" to "🇫🇷",
    "clermont_ferrand" to "🇫🇷",
    // Russia
    "sochi" to "🇷🇺",
    // Malaysia
    "sepang" to "🇲🇾",
    // South Korea
    "yeongam" to "🇰🇷",
    // India
    "buddh" to "🇮🇳",
    // Vietnam
    "hanoi" to "🇻🇳",
    // South Africa
    "kyalami" to "🇿🇦",
    // Morocco
    "ain-diab" to "🇲🇦",
    "ain_diab" to "🇲🇦",
    // Switzerland
    "bremgarten" to "🇨🇭",
    // Argentina
    "buenos_aires" to "🇦🇷",
    // Sweden
    "anderstorp" to "🇸🇪",
)

@Composable
fun RaceRowItem(
    race: Race,
    isNext: Boolean,
    modifier: Modifier = Modifier
) {
    val flag = flagMap[race.circuitId] ?: "🏁"
    val formattedTime = formatRaceTime(race.date, race.time)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = flag,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Text(
                            text = "R${race.round}",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = race.raceName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                    if (isNext) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFDC0000)
                        ) {
                            Text(
                                text = "NEXT",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "🏁 ${race.circuitName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        modifier = Modifier
                            .height(14.dp)
                            .width(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = formattedTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

private fun formatRaceTime(date: String, time: String?): String {
    return try {
        if (time != null) {
            val zdt = ZonedDateTime.parse("${date}T${time}", DateTimeFormatter.ISO_ZONED_DATE_TIME)
            val local = zdt.withZoneSameInstant(ZoneId.systemDefault())
            local.format(DateTimeFormatter.ofPattern("EEE d MMM, HH:mm", Locale.ENGLISH))
        } else {
            LocalDate.parse(date)
                .format(DateTimeFormatter.ofPattern("EEE d MMM", Locale.ENGLISH))
        }
    } catch (e: Exception) {
        date
    }
}
