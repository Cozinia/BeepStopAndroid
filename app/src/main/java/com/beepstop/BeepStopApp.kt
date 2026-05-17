package com.beepstop

import android.app.Application
import coil.Coil
import coil.ImageLoader
import com.beepstop.data.local.AppDatabase
import com.beepstop.data.local.PreferencesManager
import com.beepstop.data.remote.RetrofitClient
import com.beepstop.data.repository.RaceRepository
import com.beepstop.data.repository.StandingsRepository
import com.beepstop.worker.RaceCalendarWorker
import com.beepstop.worker.StandingsWorker
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File

class BeepStopApp : Application() {
    val database by lazy { AppDatabase.getInstance(this) }
    val raceRepository by lazy { RaceRepository(RetrofitClient.api, database.raceDao()) }
    val standingsRepository by lazy { StandingsRepository(RetrofitClient.api, database.constructorDao(), database.driverDao()) }
    val preferencesManager by lazy { PreferencesManager(this) }

    override fun onCreate() {
        super.onCreate()
        Coil.setImageLoader(
            ImageLoader.Builder(this)
                .okHttpClient(
                    OkHttpClient.Builder()
                        .cache(Cache(File(cacheDir, "image_cache"), 50L * 1024 * 1024))
                        .addInterceptor { chain ->
                            chain.proceed(
                                chain.request().newBuilder()
                                    .header("User-Agent", "BeepStopApp/1.0 (Android; F1 companion app)")
                                    .build()
                            )
                        }
                        .build()
                )
                .build()
        )
        RaceCalendarWorker.schedule(this)
        StandingsWorker.schedule(this)
    }
}
