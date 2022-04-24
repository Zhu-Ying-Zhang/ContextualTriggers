package com.example.contextualtriggers.context.room_database.Geofence

import androidx.room.*
import com.example.contextualtriggers.context.data.Geofence
import kotlinx.coroutines.flow.Flow

@Dao
interface GeofenceDao {

    @Query("SELECT * FROM geofence_tbl WHERE name = :name")
    suspend fun getGeofenceByName(name: String): Geofence?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeofence(geofence: Geofence)

    @Query("SELECT COUNT() FROM geofence_tbl WHERE name = :name")
    suspend fun count(name: String): Int

    @Update
    suspend fun update(geofence: Geofence)

    @Delete
    suspend fun deleteGeofence(geofence: Geofence)
}