package com.example.contextualtriggers.context

interface ContextAPI {

    fun noMovement(): Boolean

    suspend fun getSteps() : Int

    suspend fun addSteps(steps: Int)

    fun batteryLevel(): Int

    fun checkBatteryTriggerStatus(): Boolean

    fun changeBatteryTriggerStatus(status: Boolean)

    fun updateWeatherCodeWithLocation(weatherCode: Int)

    fun checkWeatherCodeWithLocation(): Int

    fun updateWeatherTriggerStatus(status: Boolean)

    fun checkWeatherTriggerStatus(): Boolean
}