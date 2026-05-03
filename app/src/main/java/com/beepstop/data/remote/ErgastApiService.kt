package com.beepstop.data.remote

import retrofit2.http.GET

interface ErgastApiService {

    @GET("ergast/f1/2026.json")
    suspend fun getRaceCalendar(): ErgastResponse<RaceTableData>

    @GET("ergast/f1/current/constructorstandings/")
    suspend fun getConstructorStandings(): ErgastResponse<ConstructorStandingsData>

    @GET("ergast/f1/current/driverstandings/")
    suspend fun getDriverStandings(): ErgastResponse<DriverStandingsData>
}
