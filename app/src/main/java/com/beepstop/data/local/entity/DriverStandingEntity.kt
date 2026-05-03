package com.beepstop.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "driver_standings")
data class DriverStandingEntity(
    @PrimaryKey val driverId: String,
    val name: String,
    val points: String,
    val constructorId: String,
    val cachedAt: Long
)
