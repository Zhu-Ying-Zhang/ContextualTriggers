package com.example.contextualtriggers.context.use_cases.Steps

import com.example.contextualtriggers.context.room_database.Steps.StepsRepository

class StepsExist(
    private val repository: StepsRepository
) {
    suspend operator fun invoke(date: String): Boolean {
        return repository.count(date) != 0
    }
}