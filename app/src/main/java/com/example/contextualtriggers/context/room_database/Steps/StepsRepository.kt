package com.example.contextualtriggers.context.room_database.Steps

import com.example.contextualtriggers.context.data.Steps
import kotlinx.coroutines.flow.Flow

interface StepsRepository {

    suspend fun getStepByDate(date: String): Steps?

    suspend fun insertSteps(steps: Steps)

    suspend fun count(date: String): Int

    suspend fun update(steps: Steps)

    suspend fun deleteStep(steps: Steps)
}