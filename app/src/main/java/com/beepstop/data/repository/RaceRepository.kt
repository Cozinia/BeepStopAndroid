package com.beepstop.data.repository

import com.beepstop.data.local.AppDatabase
import com.beepstop.data.local.dao.RaceDao
import com.beepstop.data.mapper.toDomain
import com.beepstop.data.mapper.toEntity
import com.beepstop.data.model.Race
import com.beepstop.data.remote.ErgastApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RaceRepository(
    private val api: ErgastApiService,
    private val raceDao: RaceDao
) {
    fun getRaces(forceRefresh: Boolean = false): Flow<List<Race>> = flow {
        val cached = raceDao.getAll()
        if (cached.isNotEmpty()) emit(cached.map { it.toDomain() })

        val needsRefresh = forceRefresh
            || cached.isEmpty()
            || !AppDatabase.isRaceCacheValid(cached.first().cachedAt)

        if (needsRefresh) {
            runCatching {
                val now = System.currentTimeMillis()
                val races = api.getRaceCalendar().mrData.raceTable.races
                raceDao.deleteAll()
                raceDao.insertAll(races.map { it.toEntity(now) })
                emit(races.map { it.toDomain() })
            }
        }
    }
}
