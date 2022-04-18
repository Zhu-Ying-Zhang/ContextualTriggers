package com.example.contextualtriggers.context.room_database

import androidx.room.*
import com.example.contextualtriggers.context.Geofence
import kotlinx.coroutines.flow.Flow

@Dao
interface GeofenceDao {

    @Query("SELECT * FROM geofence_tbl")
    fun getGeofences(): Flow<List<Geofence>>

    @Query("SELECT * FROM geofence_tbl WHERE id = :id")
    suspend fun getGeofenceById(id: Int): Geofence?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeofence(geofence: Geofence)

    @Query("SELECT COUNT() FROM geofence_tbl WHERE name = :name")
    suspend fun count(name: String): Int

    @Update
    suspend fun update(geofence: Geofence)

    @Delete
    suspend fun deleteGeofence(geofence: Geofence)
}