package com.beepstop.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.beepstop.data.local.dao.ConstructorDao
import com.beepstop.data.local.dao.DriverDao
import com.beepstop.data.local.dao.RaceDao
import com.beepstop.data.local.entity.ConstructorStandingEntity
import com.beepstop.data.local.entity.DriverStandingEntity
import com.beepstop.data.local.entity.RaceEntity

@Database(
    entities = [RaceEntity::class, ConstructorStandingEntity::class, DriverStandingEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun raceDao(): RaceDao
    abstract fun constructorDao(): ConstructorDao
    abstract fun driverDao(): DriverDao

    companion object {
        private const val TTL_RACES = 6 * 60 * 60 * 1000L
        private const val TTL_STANDINGS = 1 * 60 * 60 * 1000L

        fun isCacheValid(cachedAt: Long, ttl: Long = TTL_STANDINGS) =
            System.currentTimeMillis() - cachedAt < ttl

        fun isRaceCacheValid(cachedAt: Long) = isCacheValid(cachedAt, TTL_RACES)

        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "beepstop.db"
                ).fallbackToDestructiveMigration().build().also { instance = it }
            }
    }
}
