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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class StandingsRepository(
    private val api: ErgastApiService,
    private val constructorDao: ConstructorDao,
    private val driverDao: DriverDao
) {
    fun getConstructorStandings(forceRefresh: Boolean = false): Flow<Result<List<StandingsTeam>>> = flow {
        val cachedConstructors = constructorDao.getAll()

        val needsRefresh = forceRefresh
            || cachedConstructors.isEmpty()
            || !AppDatabase.isCacheValid(cachedConstructors.first().cachedAt)

        if (needsRefresh) {
            try {
                refreshBoth()
            } catch (e: Exception) {
                if (cachedConstructors.isEmpty()) {
                    emit(Result.failure(e))
                    return@flow
                }
            }
        }

        emitAll(
            combine(constructorDao.observeAll(), driverDao.observeAll()) { constructors, drivers ->
                val domainDrivers = drivers.map { it.toDomain() }
                Result.success(constructors.map { it.toDomain(domainDrivers) })
            }
        )
    }

    fun getDriverStandings(forceRefresh: Boolean = false): Flow<Result<List<StandingsDriver>>> = flow {
        val cached = driverDao.getAll()

        val needsRefresh = forceRefresh
            || cached.isEmpty()
            || !AppDatabase.isCacheValid(cached.first().cachedAt)

        if (needsRefresh) {
            try {
                val now = System.currentTimeMillis()
                val apiDrivers = api.getDriverStandings()
                    .mrData.standingsTable.standingsLists
                    .firstOrNull()?.driverStandings.orEmpty()
                driverDao.deleteAll()
                driverDao.insertAll(apiDrivers.map { it.toEntity(now) })
            } catch (e: Exception) {
                if (cached.isEmpty()) {
                    emit(Result.failure(e))
                    return@flow
                }
            }
        }

        emitAll(driverDao.observeAll().map { entities ->
            Result.success(entities.map { it.toDomain() })
        })
    }

    private suspend fun refreshBoth() {
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
    }
}
