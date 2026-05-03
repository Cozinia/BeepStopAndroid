package com.beepstop.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.beepstop.data.local.entity.RaceEntity

@Dao
interface RaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(races: List<RaceEntity>)

    @Query("SELECT * FROM races ORDER BY round ASC")
    suspend fun getAll(): List<RaceEntity>

    @Query("DELETE FROM races")
    suspend fun deleteAll()
}
