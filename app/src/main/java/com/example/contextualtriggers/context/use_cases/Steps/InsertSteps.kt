package com.example.contextualtriggers.context.use_cases.Steps

import com.example.contextualtriggers.context.data.Steps
import com.example.contextualtriggers.context.room_database.Steps.StepsRepository

class InsertSteps (
    private val repository: StepsRepository
) {
    suspend operator fun invoke(steps: Steps) {
        repository.insertSteps(steps)
    }
}