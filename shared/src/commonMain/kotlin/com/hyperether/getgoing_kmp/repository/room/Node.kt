package com.hyperether.getgoing_kmp.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Route::class,
        parentColumns = ["id"],
        childColumns = ["routeId"],
        onDelete = CASCADE
    )], indices = [Index(value = ["routeId"])]
)
data class Node(
    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @ColumnInfo
    var latitude: Double,

    @ColumnInfo
    var longitude: Double,

    @ColumnInfo
    var velocity: Float,

    @ColumnInfo(name = "number")
    var index: Long, // node index within a particular route

    @ColumnInfo
    var isLast: Boolean = false,

    var routeId: Long, // foreign key

)
