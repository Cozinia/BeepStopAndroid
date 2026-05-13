package com.beepstop.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.beepstop.data.local.entity.ConstructorStandingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConstructorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(constructors: List<ConstructorStandingEntity>)

    @Query("SELECT * FROM constructor_standings ORDER BY CAST(points AS REAL) DESC")
    fun observeAll(): Flow<List<ConstructorStandingEntity>>

    @Query("SELECT * FROM constructor_standings ORDER BY CAST(points AS REAL) DESC")
    suspend fun getAll(): List<ConstructorStandingEntity>

    @Query("DELETE FROM constructor_standings")
    suspend fun deleteAll()
}
