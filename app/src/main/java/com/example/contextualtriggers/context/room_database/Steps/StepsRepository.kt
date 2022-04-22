package com.example.contextualtriggers.context.room_database.Steps

import com.example.contextualtriggers.context.Steps
import kotlinx.coroutines.flow.Flow

interface StepsRepository {

    fun getSteps(): Flow<List<Steps>>

    suspend fun getStepByDate(date: String): Steps?

    suspend fun insertSteps(steps: Steps)

    suspend fun count(date: String): Int

    suspend fun update(steps: Steps)

    suspend fun deleteStep(steps: Steps)
}