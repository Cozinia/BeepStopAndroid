package com.beepstop.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "races")
data class RaceEntity(
    @PrimaryKey val round: Int,
    val raceName: String,
    val date: String,
    val time: String?,
    val circuitId: String,
    val circuitName: String,
    val cachedAt: Long
)
