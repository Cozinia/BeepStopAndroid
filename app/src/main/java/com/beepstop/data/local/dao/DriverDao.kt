package com.beepstop.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.beepstop.data.local.entity.DriverStandingEntity

@Dao
interface DriverDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(drivers: List<DriverStandingEntity>)

    @Query("SELECT * FROM driver_standings ORDER BY CAST(points AS REAL) DESC")
    suspend fun getAll(): List<DriverStandingEntity>

    @Query("DELETE FROM driver_standings")
    suspend fun deleteAll()
}
