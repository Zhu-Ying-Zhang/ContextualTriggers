package com.example.contextualtriggers.context.room_database.Geofence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.contextualtriggers.context.Geofence

@Database(entities = [Geofence::class], version = 3, exportSchema = false)
abstract class GeofenceDatabase: RoomDatabase() {

    abstract val geofenceDao: GeofenceDao

    companion object {
        const val DATABASE_NAME = "geofence_db"
    }
}