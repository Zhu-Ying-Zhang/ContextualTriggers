package com.example.contextualtriggers.context.room_database.Steps

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.contextualtriggers.context.Steps

@Database(entities = [Steps::class], version = 3, exportSchema = false)
abstract class StepsDatabase: RoomDatabase() {

    abstract val stepsDao: StepsDao

    companion object {
        const val DATABASE_NAME = "steps_db"

        @Volatile
        var INSTANCE: StepsDatabase? = null

        fun getInstance(context: Context): StepsDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        StepsDatabase::class.java,
                        DATABASE_NAME
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