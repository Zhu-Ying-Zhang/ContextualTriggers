package com.example.contextualtriggers.context

interface ContextAPI {

    fun noMovement(): Boolean

    suspend fun getSteps() : Int

    suspend fun addSteps(steps: Int)

    fun getGoal() : Int

    fun batteryLevel(): Int

    fun checkBatteryTriggerStatus(): Boolean

    fun changeBatteryTriggerStatus(status: Boolean)

    fun isInEvent(): Boolean
}