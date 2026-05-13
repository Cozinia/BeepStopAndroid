package com.beepstop.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.beepstop.data.local.entity.RaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(races: List<RaceEntity>)

    @Query("SELECT * FROM races ORDER BY round ASC")
    fun observeAll(): Flow<List<RaceEntity>>

    @Query("SELECT * FROM races ORDER BY round ASC")
    suspend fun getAll(): List<RaceEntity>

    @Query("SELECT * FROM races WHERE date >= :today ORDER BY round ASC")
    suspend fun getUpcoming(today: String): List<RaceEntity>

    @Query("DELETE FROM races")
    suspend fun deleteAll()
}
