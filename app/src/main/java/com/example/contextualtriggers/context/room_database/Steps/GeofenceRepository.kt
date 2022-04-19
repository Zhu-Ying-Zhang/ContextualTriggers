package com.example.contextualtriggers.context.room_database.Steps

import com.example.contextualtriggers.context.Geofence
import kotlinx.coroutines.flow.Flow

interface GeofenceRepository {

    fun getGeofences(): Flow<List<Geofence>>

    suspend fun getGeofenceById(id: Int): Geofence?

    suspend fun insertGeofence(geofence: Geofence)

    suspend fun count(title: String): Int

    suspend fun update(geofence: Geofence)

    suspend fun deleteGeofence(geofence: Geofence)
}