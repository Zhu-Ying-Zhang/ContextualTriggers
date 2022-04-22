package com.example.contextualtriggers.context.use_cases.Geofence

import com.example.contextualtriggers.context.Geofence
import com.example.contextualtriggers.context.Steps
import com.example.contextualtriggers.context.room_database.Geofence.GeofenceRepository

class GetGeofence(
    private val repository: GeofenceRepository
) {
    suspend operator fun invoke(name: String): Geofence? {
        return repository.getGeofenceByName(name)
    }
}