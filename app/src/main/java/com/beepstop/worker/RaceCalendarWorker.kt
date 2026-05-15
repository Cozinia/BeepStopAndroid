package com.beepstop.worker

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.beepstop.BeepStopApp
import com.beepstop.data.SessionFinder
import com.beepstop.data.local.PreferencesManager
import com.beepstop.widget.CircuitWidget
import com.beepstop.widget.CountdownWidget
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

class RaceCalendarWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val app = context.applicationContext as BeepStopApp
            val prefs = app.preferencesManager
            val countdownTarget = prefs.observe(PreferencesManager.KEY_COUNTDOWN_TARGET, "race").first()

            val races = app.raceRepository.getRacesOnce()
            val session = SessionFinder.findNext(races, countdownTarget)

            if (session != null) {
                val nextRace = races.find { it.raceName == session.raceName }
                prefs.save(PreferencesManager.KEY_NEXT_SESSION_DATE, session.targetDate.toEpochMilli())
                prefs.save(PreferencesManager.KEY_NEXT_SESSION_LABEL, session.label)
                prefs.save(PreferencesManager.KEY_NEXT_SESSION_RACE, session.raceName)
                prefs.save(PreferencesManager.KEY_NEXT_CIRCUIT_ID,   nextRace?.circuit?.circuitId   ?: "")
                prefs.save(PreferencesManager.KEY_NEXT_CIRCUIT_NAME, nextRace?.circuit?.circuitName ?: "")
            }

            val lastExpired = prefs.observe(PreferencesManager.KEY_LAST_EXPIRED_DATE, 0L).first()
            val lastReload = prefs.observe(PreferencesManager.KEY_LAST_STANDINGS_RELOAD, 0L).first()
            val twoHourTen = 2 * 60 * 60 * 1000L + 10 * 60 * 1000L
            if (lastExpired > 0
                && System.currentTimeMillis() >= lastExpired + twoHourTen
                && lastReload < lastExpired
            ) {
                prefs.save(PreferencesManager.KEY_LAST_STANDINGS_RELOAD, System.currentTimeMillis())
                WorkManager.getInstance(context).enqueue(
                    OneTimeWorkRequestBuilder<StandingsWorker>().build()
                )
            }

            CircuitWidget().updateAll(context)
            CountdownWidget().updateAll(context)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        private const val WORK_NAME = "race_calendar_refresh"

        fun schedule(context: Context) {
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                PeriodicWorkRequestBuilder<RaceCalendarWorker>(6, TimeUnit.HOURS).build()
            )
        }
    }
}
