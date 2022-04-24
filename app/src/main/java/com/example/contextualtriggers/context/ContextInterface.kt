package com.example.contextualtriggers.context

interface ContextInterface {

    fun noMovement(): Boolean

    suspend fun getSteps() : Int

    suspend fun addSteps(steps: Int)

    fun getGoal() : Int

    fun batteryLevel(): Int

    fun checkBatteryTriggerStatus(): Boolean

    fun changeBatteryTriggerStatus(status: Boolean)

    fun isInEvent(): Boolean

    fun updateWeatherCodeWithLocation(weatherCode: Int)

    fun checkWeatherCodeWithLocation(): Int

    fun updateWeatherTriggerStatus(status: Boolean)

    fun checkWeatherTriggerStatus(): Boolean

    fun updateWeatherCodeWithAlarm(weatherCode: Int)

    fun checkWeatherCodeWithAlarm(): Int

    fun updateWeatherWithAlarmTriggerStatus(status: Boolean)

    fun checkWeatherWithAlarmTriggerStatus(): Boolean
}