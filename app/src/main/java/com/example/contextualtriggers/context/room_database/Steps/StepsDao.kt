package com.example.contextualtriggers.context.room_database.Steps

import androidx.room.*
import com.example.contextualtriggers.context.data.Steps
import kotlinx.coroutines.flow.Flow

@Dao
interface StepsDao {

    @Query("SELECT * FROM steps_tbl WHERE date = :date")
    suspend fun getStepByDate(date: String): Steps?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStep(steps: Steps)

    @Query("SELECT COUNT() FROM steps_tbl WHERE date = :date")
    suspend fun count(date: String): Int

    @Update
    suspend fun update(steps: Steps)

    @Delete
    suspend fun deleteStep(steps: Steps)
}