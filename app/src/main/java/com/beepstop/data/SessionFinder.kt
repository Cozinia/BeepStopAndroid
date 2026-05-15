package com.beepstop.data

import com.beepstop.data.remote.ApiRace
import com.beepstop.data.remote.ApiSessionTime
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class StoredSession(
    val targetDate: Instant,
    val label: String,
    val raceName: String
)

object SessionFinder {
    fun findNext(races: List<ApiRace>, countdownTarget: String): StoredSession? {
        val now = Instant.now()
        for (race in races.sortedBy { it.date }) {
            val sessions: List<Pair<ApiSessionTime, String>> = listOfNotNull(
                race.firstPractice?.let { it to "Practice" },
                race.secondPractice?.let { it to "Practice" },
                race.thirdPractice?.let { it to "Practice" },
                race.sprint?.let { it to "Sprint" },
                race.qualifying?.let { it to "Qualifying" },
                race.time?.let { ApiSessionTime(race.date, it) to "Race" },
            )
            for ((session, label) in sessions) {
                val time = session.time ?: continue
                val targetDate = try {
                    ZonedDateTime.parse(
                        "${session.date}T$time",
                        DateTimeFormatter.ISO_ZONED_DATE_TIME
                    ).toInstant()
                } catch (e: Exception) {
                    continue
                }
                if (targetDate <= now) continue
                val matches = countdownTarget == "all"
                    || countdownTarget.equals(label, ignoreCase = true)
                if (matches) return StoredSession(targetDate, label, race.raceName)
            }
        }
        return null
    }
}
