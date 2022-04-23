package com.example.contextualtriggers.context

interface ContextAPI {

    fun noMovement(): Boolean

    suspend fun getSteps() : Int

    suspend fun addSteps(steps: Int)

    fun batteryLevel(): Int

    fun checkBatteryTriggerStatus(): Boolean
}