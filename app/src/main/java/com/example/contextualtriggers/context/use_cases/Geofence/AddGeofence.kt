package com.example.contextualtriggers.context.use_cases

import com.example.contextualtriggers.context.Geofence
import com.example.contextualtriggers.context.room_database.GeofenceRepository

class AddGeofence (
    private val repository: GeofenceRepository
) {
    suspend operator fun invoke(geofence: Geofence) {
        repository.insertGeofence(geofence)
    }
}