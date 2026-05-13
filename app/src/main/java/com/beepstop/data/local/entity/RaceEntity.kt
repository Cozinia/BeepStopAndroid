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
    val cachedAt: Long,
    val fp1Date: String? = null,
    val fp1Time: String? = null,
    val fp2Date: String? = null,
    val fp2Time: String? = null,
    val fp3Date: String? = null,
    val fp3Time: String? = null,
    val sprintDate: String? = null,
    val sprintTime: String? = null,
    val qualifyingDate: String? = null,
    val qualifyingTime: String? = null,
)
