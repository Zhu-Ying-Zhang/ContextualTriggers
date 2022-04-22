package com.example.contextualtriggers.context.use_cases.Steps

import com.example.contextualtriggers.context.Steps
import com.example.contextualtriggers.context.room_database.Steps.StepsRepository

class GetSteps (
private val repository: StepsRepository
) {
    suspend operator fun invoke(date: String): Steps? {
        return repository.getStepByDate(date)
    }
}