package com.example.contextualtriggers.context.room_database.Geofence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.contextualtriggers.context.data.Geofence

@Database(entities = [Geofence::class], version = 3, exportSchema = false)
abstract class GeofenceDatabase: RoomDatabase() {

    abstract val geofenceDao: GeofenceDao

    companion object {
        const val DATABASE_NAME = "geofence_db"

        @Volatile
        var INSTANCE: GeofenceDatabase? = null

        fun getInstance(context: Context): GeofenceDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        GeofenceDatabase::class.java,
                        GeofenceDatabase.DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}