package com.beepstop.data.repository

import com.beepstop.data.local.AppDatabase
import com.beepstop.data.local.dao.RaceDao
import com.beepstop.data.mapper.toDomain
import com.beepstop.data.mapper.toApiRace
import com.beepstop.data.mapper.toEntity
import com.beepstop.data.model.Race
import com.beepstop.data.remote.ApiRace
import com.beepstop.data.remote.ErgastApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class RaceRepository(
    private val api: ErgastApiService,
    private val raceDao: RaceDao
) {
    fun getRaces(forceRefresh: Boolean = false): Flow<Result<List<Race>>> = flow {
        val cached = raceDao.getAll()

        val needsRefresh = forceRefresh
            || cached.isEmpty()
            || !AppDatabase.isRaceCacheValid(cached.first().cachedAt)

        if (needsRefresh) {
            try {
                val now = System.currentTimeMillis()
                val races = api.getRaceCalendar().mrData.raceTable.races
                raceDao.deleteAll()
                raceDao.insertAll(races.map { it.toEntity(now) })
            } catch (e: Exception) {
                if (cached.isEmpty()) {
                    emit(Result.failure(e))
                    return@flow
                }
            }
        }

        emitAll(raceDao.observeAll().map { entities -> Result.success(entities.map { it.toDomain() }) })
    }

    suspend fun getRacesOnce(): List<ApiRace> {
        val cached = raceDao.getAll()
        if (cached.isNotEmpty() && AppDatabase.isRaceCacheValid(cached.first().cachedAt)) {
            return cached.map { it.toApiRace() }
        }
        val now = System.currentTimeMillis()
        val races = api.getRaceCalendar().mrData.raceTable.races
        raceDao.deleteAll()
        raceDao.insertAll(races.map { it.toEntity(now) })
        return races
    }
}
