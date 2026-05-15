package com.beepstop.data.mapper

import com.beepstop.data.local.entity.ConstructorStandingEntity
import com.beepstop.data.local.entity.DriverStandingEntity
import com.beepstop.data.local.entity.RaceEntity
import com.beepstop.data.model.Race
import com.beepstop.data.model.StandingsDriver
import com.beepstop.data.model.StandingsTeam
import com.beepstop.data.remote.ApiCircuit
import com.beepstop.data.remote.ApiConstructorStanding
import com.beepstop.data.remote.ApiDriverStanding
import com.beepstop.data.remote.ApiRace
import com.beepstop.data.remote.ApiSessionTime

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

fun ApiRace.toEntity(cachedAt: Long) = RaceEntity(
    round = round.toIntOrNull() ?: 0,
    raceName = raceName,
    date = date,
    time = time,
    circuitId = circuit.circuitId,
    circuitName = circuit.circuitName,
    cachedAt = cachedAt,
    fp1Date = firstPractice?.date,
    fp1Time = firstPractice?.time,
    fp2Date = secondPractice?.date,
    fp2Time = secondPractice?.time,
    fp3Date = thirdPractice?.date,
    fp3Time = thirdPractice?.time,
    sprintDate = sprint?.date,
    sprintTime = sprint?.time,
    qualifyingDate = qualifying?.date,
    qualifyingTime = qualifying?.time,
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

fun RaceEntity.toDomain() = Race(
    round = round,
    raceName = raceName,
    date = date,
    time = time,
    circuitId = circuitId,
    circuitName = circuitName
)

fun RaceEntity.toApiRace() = ApiRace(
    round = round.toString(),
    raceName = raceName,
    date = date,
    time = time,
    circuit = ApiCircuit(circuitId, circuitName),
    firstPractice = if (fp1Date != null) ApiSessionTime(fp1Date, fp1Time) else null,
    secondPractice = if (fp2Date != null) ApiSessionTime(fp2Date, fp2Time) else null,
    thirdPractice = if (fp3Date != null) ApiSessionTime(fp3Date, fp3Time) else null,
    sprint = if (sprintDate != null) ApiSessionTime(sprintDate, sprintTime) else null,
    qualifying = if (qualifyingDate != null) ApiSessionTime(qualifyingDate, qualifyingTime) else null,
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
