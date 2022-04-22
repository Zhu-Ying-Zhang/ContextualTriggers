package com.example.contextualtriggers.context.room_database.Steps

import com.example.contextualtriggers.context.Steps
import kotlinx.coroutines.flow.Flow

class StepsRepoImplementation (
    private val dao: StepsDao
) : StepsRepository {

    override fun getSteps(): Flow<List<Steps>> {
        return dao.getSteps()
    }

    override suspend fun getStepByDate(date: String): Steps? {
        return dao.getStepByDate(date)
    }

    override suspend fun insertSteps(steps: Steps) {
        return dao.insertStep(steps)
    }

    override suspend fun count(date: String): Int {
        return dao.count(date)
    }

    override suspend fun update(steps: Steps) {
        return dao.update(steps)
    }

    override suspend fun deleteStep(steps: Steps) {
        return dao.deleteStep(steps)
    }
}
