package com.hyperether.getgoing_kmp.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Route(
    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @ColumnInfo
    var duration: Long, // duration in milliseconds

    @ColumnInfo
    var energy: Double,

    @ColumnInfo
    var length: Double,

    @ColumnInfo
    var date: String,

    @ColumnInfo
    var avgSpeed: Double, // average speed during this route

    @ColumnInfo
    var currentSpeed: Double,

    @ColumnInfo
    var activity_id: Int, // actitivy called for this route walk = 1, run = 2, ride = 3

    @ColumnInfo
    var goal: Long
)
