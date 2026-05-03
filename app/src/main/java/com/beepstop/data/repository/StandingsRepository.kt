package com.beepstop.data.repository

import com.beepstop.data.local.AppDatabase
import com.beepstop.data.local.dao.ConstructorDao
import com.beepstop.data.local.dao.DriverDao
import com.beepstop.data.mapper.toDomain
import com.beepstop.data.mapper.toEntity
import com.beepstop.data.model.StandingsDriver
import com.beepstop.data.model.StandingsTeam
import com.beepstop.data.remote.ErgastApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StandingsRepository(
    private val api: ErgastApiService,
    private val constructorDao: ConstructorDao,
    private val driverDao: DriverDao
) {
    fun getConstructorStandings(forceRefresh: Boolean = false): Flow<List<StandingsTeam>> = flow {
        val cachedConstructors = constructorDao.getAll()
        val cachedDrivers = driverDao.getAll().map { it.toDomain() }

        if (cachedConstructors.isNotEmpty()) {
            emit(cachedConstructors.map { it.toDomain(cachedDrivers) })
        }

        val needsRefresh = forceRefresh
            || cachedConstructors.isEmpty()
            || !AppDatabase.isCacheValid(cachedConstructors.first().cachedAt)

        if (needsRefresh) {
            runCatching {
                val now = System.currentTimeMillis()
                val apiDrivers = api.getDriverStandings()
                    .mrData.standingsTable.standingsLists
                    .firstOrNull()?.driverStandings.orEmpty()
                val apiConstructors = api.getConstructorStandings()
                    .mrData.standingsTable.standingsLists
                    .firstOrNull()?.constructorStandings.orEmpty()

                driverDao.deleteAll()
                driverDao.insertAll(apiDrivers.map { it.toEntity(now) })
                constructorDao.deleteAll()
                constructorDao.insertAll(apiConstructors.map { it.toEntity(now) })

                val drivers = apiDrivers.map { it.toDomain() }
                emit(apiConstructors.map { it.toDomain(drivers) })
            }
        }
    }

    fun getDriverStandings(forceRefresh: Boolean = false): Flow<List<StandingsDriver>> = flow {
        val cached = driverDao.getAll()
        if (cached.isNotEmpty()) emit(cached.map { it.toDomain() })

        val needsRefresh = forceRefresh
            || cached.isEmpty()
            || !AppDatabase.isCacheValid(cached.first().cachedAt)

        if (needsRefresh) {
            runCatching {
                val now = System.currentTimeMillis()
                val apiDrivers = api.getDriverStandings()
                    .mrData.standingsTable.standingsLists
                    .firstOrNull()?.driverStandings.orEmpty()

                driverDao.deleteAll()
                driverDao.insertAll(apiDrivers.map { it.toEntity(now) })
                emit(apiDrivers.map { it.toDomain() })
            }
        }
    }
}
