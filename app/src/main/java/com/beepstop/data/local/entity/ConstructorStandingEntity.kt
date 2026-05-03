package com.beepstop.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "constructor_standings")
data class ConstructorStandingEntity(
    @PrimaryKey val constructorId: String,
    val name: String,
    val points: String,
    val cachedAt: Long
)
