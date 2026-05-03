package com.beepstop.data.remote

import com.google.gson.annotations.SerializedName

data class ErgastResponse<T>(
    @SerializedName("MRData") val mrData: T
)

data class RaceTableData(
    @SerializedName("RaceTable") val raceTable: RaceTable
)

data class RaceTable(
    @SerializedName("Races") val races: List<ApiRace>
)

data class ConstructorStandingsData(
    @SerializedName("StandingsTable") val standingsTable: ConstructorStandingsTable
)

data class ConstructorStandingsTable(
    @SerializedName("StandingsLists") val standingsLists: List<ConstructorStandingsList>
)

data class ConstructorStandingsList(
    @SerializedName("ConstructorStandings") val constructorStandings: List<ApiConstructorStanding>
)

data class DriverStandingsData(
    @SerializedName("StandingsTable") val standingsTable: DriverStandingsTable
)

data class DriverStandingsTable(
    @SerializedName("StandingsLists") val standingsLists: List<DriverStandingsList>
)

data class DriverStandingsList(
    @SerializedName("DriverStandings") val driverStandings: List<ApiDriverStanding>
)
