package com.example.contextualtriggers.context.room_database

import com.example.contextualtriggers.context.Geofence
import kotlinx.coroutines.flow.Flow

class GeofenceRepoImplementation (
    private val dao: GeofenceDao
) : GeofenceRepository {

    override fun getGeofences(): Flow<List<Geofence>> {
        return dao.getGeofences()
    }

    override suspend fun getGeofenceById(id: Int): Geofence? {
        return dao.getGeofenceById(id)
    }

    override suspend fun insertGeofence(geofence: Geofence) {
        return dao.insertGeofence(geofence)
    }

    override suspend fun count(name: String): Int {
        return dao.count(name)
    }

    override suspend fun update(geofence: Geofence) {
        return dao.update(geofence)
    }

    override suspend fun deleteGeofence(geofence: Geofence) {
        return dao.deleteGeofence(geofence)
    }
}