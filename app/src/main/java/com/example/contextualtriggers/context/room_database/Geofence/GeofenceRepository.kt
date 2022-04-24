package com.example.contextualtriggers.context.room_database.Geofence

import com.example.contextualtriggers.context.data.Geofence
import kotlinx.coroutines.flow.Flow

interface GeofenceRepository {

    suspend fun getGeofenceByName(name: String): Geofence?

    suspend fun insertGeofence(geofence: Geofence)

    suspend fun count(title: String): Int

    suspend fun update(geofence: Geofence)

    suspend fun deleteGeofence(geofence: Geofence)
}