package com.beepstop

import android.app.Application
import com.beepstop.data.local.AppDatabase
import com.beepstop.data.local.PreferencesManager
import com.beepstop.data.remote.RetrofitClient
import com.beepstop.data.repository.RaceRepository
import com.beepstop.data.repository.StandingsRepository

class BeepStopApp : Application() {
    val database by lazy { AppDatabase.getInstance(this) }
    val raceRepository by lazy { RaceRepository(RetrofitClient.api, database.raceDao()) }
    val standingsRepository by lazy { StandingsRepository(RetrofitClient.api, database.constructorDao(), database.driverDao()) }
    val preferencesManager by lazy { PreferencesManager(this) }
}
