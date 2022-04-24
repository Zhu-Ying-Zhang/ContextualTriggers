package com.example.contextualtriggers.context.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "geofence_tbl")
class Geofence(
    @PrimaryKey
    val name: String,
    val latitude: Double,
    val longitude: Double
) {
}