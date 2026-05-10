package com.beepstop.worker

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.beepstop.BeepStopApp
import com.beepstop.data.local.PreferencesManager
import com.beepstop.data.mapper.toDomain
import com.beepstop.widget.ImageCacheHelper
import com.beepstop.widget.StandingsWidget
import com.beepstop.widget.model.StandingsWidgetItem
import com.google.gson.Gson
import java.util.concurrent.TimeUnit

class StandingsWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val app = context.applicationContext as BeepStopApp
            val prefs = app.preferencesManager
            val api = com.beepstop.data.remote.RetrofitClient.api

            val apiConstructors = api.getConstructorStandings()
                .mrData.standingsTable.standingsLists
                .firstOrNull()?.constructorStandings.orEmpty()
            val apiDrivers = api.getDriverStandings()
                .mrData.standingsTable.standingsLists
                .firstOrNull()?.driverStandings.orEmpty()

            val drivers = apiDrivers.map { it.toDomain() }
            val teams = apiConstructors.map { it.toDomain(drivers) }

            val items = mutableListOf<StandingsWidgetItem>()
            teams.forEach { team ->
                items.add(StandingsWidgetItem(
                    id = team.id, name = team.name, code = "", points = team.points,
                    constructorId = team.id, mode = "team"
                ))
                ImageCacheHelper.getCachedLogo(context, team.id)
            }
            drivers.forEach { driver ->
                items.add(StandingsWidgetItem(
                    id = driver.id, name = driver.name, code = driver.code,
                    points = driver.points, constructorId = driver.constructorId, mode = "driver"
                ))
            }

            prefs.save(PreferencesManager.KEY_STANDINGS_CACHE, Gson().toJson(items))
            StandingsWidget().updateAll(context)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        private const val WORK_NAME = "standings_refresh"

        fun schedule(context: Context) {
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                PeriodicWorkRequestBuilder<StandingsWorker>(1, TimeUnit.HOURS).build()
            )
        }
    }
}
