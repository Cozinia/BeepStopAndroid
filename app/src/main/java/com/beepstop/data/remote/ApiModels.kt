package com.beepstop.data.remote

import com.google.gson.annotations.SerializedName

data class ApiRace(
    val round: String,
    val raceName: String,
    val date: String,
    val time: String?,
    @SerializedName("Circuit") val circuit: ApiCircuit,
    @SerializedName("FirstPractice") val firstPractice: ApiSessionTime? = null,
    @SerializedName("SecondPractice") val secondPractice: ApiSessionTime? = null,
    @SerializedName("ThirdPractice") val thirdPractice: ApiSessionTime? = null,
    @SerializedName("Sprint") val sprint: ApiSessionTime? = null,
    @SerializedName("Qualifying") val qualifying: ApiSessionTime? = null,
)

data class ApiSessionTime(
    val date: String,
    val time: String?
)

data class ApiCircuit(
    val circuitId: String,
    val circuitName: String
)

data class ApiConstructorStanding(
    val position: String,
    val points: String,
    @SerializedName("Constructor") val constructor: ApiConstructor
)

data class ApiConstructor(
    val constructorId: String,
    val name: String
)

data class ApiDriverStanding(
    val position: String,
    val points: String,
    @SerializedName("Driver") val driver: ApiDriver,
    @SerializedName("Constructors") val constructors: List<ApiDriverConstructor>
)

data class ApiDriver(
    val driverId: String,
    val givenName: String,
    val familyName: String,
    val code: String?
)

data class ApiDriverConstructor(
    val constructorId: String,
    val name: String
)
