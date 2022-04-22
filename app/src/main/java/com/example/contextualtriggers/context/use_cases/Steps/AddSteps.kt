package com.example.contextualtriggers.context.use_cases.Steps

import com.example.contextualtriggers.context.room_database.Steps.StepsRepository

class AddSteps (
    private val repository: StepsRepository
) {
    suspend operator fun invoke(date: String, steps: Int) {
        var todaysSteps = repository.getStepByDate(date)
        if (todaysSteps != null) {
            todaysSteps.steps = todaysSteps.steps + steps
            repository.update(todaysSteps)
        }
    }
}