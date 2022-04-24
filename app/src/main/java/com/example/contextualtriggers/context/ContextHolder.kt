package com.example.contextualtriggers.context

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import com.example.contextualtriggers.context.room_database.Steps.util.CurrentDate
import com.example.contextualtriggers.context.room_database.Steps.util.FormatDate
import com.example.contextualtriggers.context.use_cases.Geofence.GeofenceUseCases
import com.example.contextualtriggers.context.use_cases.Steps.StepsUseCases
import javax.inject.Inject

class ContextHolder constructor(
    context: Context,
    private val geofenceUseCases: GeofenceUseCases,
    private val stepsUseCases: StepsUseCases
): ContextAPI {

    var batteryLevel: Int = 0
    private var context: Context = context
    var noMovement = false
    private var batteryTriggerStatus = false
    var todaysEvents: ArrayList<CalendarEvent>? = null
    private var weatherWithLocation = 0
    private var weatherTriggerStatus = false

    private var weatherWithAlarm = 0
    private var weatherAlarmTriggerStatus = false

    override fun noMovement(): Boolean = noMovement

    fun nextEvent() {
        if(todaysEvents.isNullOrEmpty()) {
            Log.d("Upcoming Events", "No events")
        }
        else {
            Log.d("Upcoming Events", "${todaysEvents!![0].title}")
        }
    }

    override suspend fun getSteps() : Int {
        val date = CurrentDate()
        if(stepsUseCases.stepsExist(date)) {
            val steps = stepsUseCases.getSteps(date)
            if(steps != null)
                return steps.steps
        }
        return 0
    }

    override suspend fun addSteps(steps: Int) {
        val date = CurrentDate()
        if(stepsUseCases.stepsExist(date)) {
            stepsUseCases.addSteps(date, steps)
        }
        else {
            stepsUseCases.insertSteps(
                Steps(
                    date,
                    steps
                )
            )
        }
    }

    override fun batteryLevel(): Int = batteryLevel

    override fun checkBatteryTriggerStatus(): Boolean = batteryTriggerStatus

    override fun changeBatteryTriggerStatus(status: Boolean) {
        batteryTriggerStatus = status
    }

    override fun updateWeatherCodeWithLocation(weatherCode: Int) {
        weatherWithLocation = weatherCode
    }

    override fun checkWeatherCodeWithLocation(): Int = weatherWithLocation

    override fun updateWeatherTriggerStatus(status: Boolean) {
        Log.d("checkWeatherTriggerStatus-Before", weatherTriggerStatus.toString())
        weatherTriggerStatus = status
        Log.d("checkWeatherTriggerStatus-After", weatherTriggerStatus.toString())
    }

    override fun checkWeatherTriggerStatus(): Boolean = weatherTriggerStatus

    override fun updateWeatherCodeWithAlarm(weatherCode: Int) {
        weatherWithAlarm = weatherCode
    }

    override fun checkWeatherCodeWithAlarm(): Int = weatherWithAlarm

    override fun updateWeatherWithAlarmTriggerStatus(status: Boolean) {
        weatherAlarmTriggerStatus = status
    }

    override fun checkWeatherWithAlarmTriggerStatus(): Boolean = weatherAlarmTriggerStatus
}