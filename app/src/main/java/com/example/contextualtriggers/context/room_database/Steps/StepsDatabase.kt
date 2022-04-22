package com.example.contextualtriggers.context.room_database.Steps

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.contextualtriggers.context.Steps

@Database(entities = [Steps::class], version = 2, exportSchema = false)
abstract class StepsDatabase: RoomDatabase() {

    abstract val stepsDao: StepsDao

    companion object {
        const val DATABASE_NAME = "steps_db"
    }
}