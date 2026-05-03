package com.beepstop.data.mapper

import com.beepstop.data.local.entity.ConstructorStandingEntity
import com.beepstop.data.local.entity.DriverStandingEntity
import com.beepstop.data.local.entity.RaceEntity
import com.beepstop.data.model.Race
import com.beepstop.data.model.StandingsDriver
import com.beepstop.data.model.StandingsTeam
import com.beepstop.data.remote.ApiConstructorStanding
import com.beepstop.data.remote.ApiDriverStanding
import com.beepstop.data.remote.ApiRace

// API → Domain
fun ApiRace.toDomain() = Race(
    round = round.toIntOrNull() ?: 0,
    raceName = raceName,
    date = date,
    time = time,
    circuitId = circuit.circuitId,
    circuitName = circuit.circuitName
)

fun ApiDriverStanding.toDomain() = StandingsDriver(
    id = driver.driverId,
    code = driver.code ?: driver.driverId.take(3).uppercase(),
    name = "${driver.givenName} ${driver.familyName}",
    points = points,
    constructorId = constructors.firstOrNull()?.constructorId.orEmpty()
)

fun ApiConstructorStanding.toDomain(drivers: List<StandingsDriver>) = StandingsTeam(
    id = constructor.constructorId,
    name = constructor.name,
    points = points,
    drivers = drivers.filter { it.constructorId == constructor.constructorId }
)

// API → Entity
fun ApiRace.toEntity(cachedAt: Long) = RaceEntity(
    round = round.toIntOrNull() ?: 0,
    raceName = raceName,
    date = date,
    time = time,
    circuitId = circuit.circuitId,
    circuitName = circuit.circuitName,
    cachedAt = cachedAt
)

fun ApiDriverStanding.toEntity(cachedAt: Long) = DriverStandingEntity(
    driverId = driver.driverId,
    code = driver.code ?: driver.driverId.take(3).uppercase(),
    name = "${driver.givenName} ${driver.familyName}",
    points = points,
    constructorId = constructors.firstOrNull()?.constructorId.orEmpty(),
    cachedAt = cachedAt
)

fun ApiConstructorStanding.toEntity(cachedAt: Long) = ConstructorStandingEntity(
    constructorId = constructor.constructorId,
    name = constructor.name,
    points = points,
    cachedAt = cachedAt
)

// Entity → Domain
fun RaceEntity.toDomain() = Race(
    round = round,
    raceName = raceName,
    date = date,
    time = time,
    circuitId = circuitId,
    circuitName = circuitName
)

fun DriverStandingEntity.toDomain() = StandingsDriver(
    id = driverId,
    code = code,
    name = name,
    points = points,
    constructorId = constructorId
)

fun ConstructorStandingEntity.toDomain(drivers: List<StandingsDriver>) = StandingsTeam(
    id = constructorId,
    name = name,
    points = points,
    drivers = drivers.filter { it.constructorId == constructorId }
)
