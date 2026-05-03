package com.beepstop.data.model

data class Race(
    val round: Int,
    val raceName: String,
    val date: String,
    val time: String?,
    val circuitId: String,
    val circuitName: String
)

data class StandingsTeam(
    val id: String,
    val name: String,
    val points: String,
    val drivers: List<StandingsDriver>
)

data class StandingsDriver(
    val id: String,
    val name: String,
    val points: String,
    val constructorId: String
)
