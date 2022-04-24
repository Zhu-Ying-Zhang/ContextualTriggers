package com.example.contextualtriggers.context

import android.content.Context
import android.util.Log
import com.example.contextualtriggers.context.data.CalendarEvent
import com.example.contextualtriggers.context.data.Steps
import com.example.contextualtriggers.context.util.CurrentDate
import com.example.contextualtriggers.context.use_cases.Geofence.GeofenceUseCases
import com.example.contextualtriggers.context.use_cases.Steps.StepsUseCases
import com.example.contextualtriggers.context.util.CurrentDateTime

class ContextState constructor(
    context: Context,
    private val geofenceUseCases: GeofenceUseCases,
    private val stepsUseCases: StepsUseCases
): ContextInterface {

    var batteryLevel: Int = 0
    private var context: Context = context
    var noMovement = false
    private var batteryTriggerStatus = false
    var todaysEvents: ArrayList<CalendarEvent>? = null
    private var weatherWithLocation = 0
    private var weatherTriggerStatus = false

    private var weatherWithAlarm = 0
    private var weatherAlarmTriggerStatus = false

    var stepsGoal: Int = 10000

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

    override fun getGoal() : Int {
        return stepsGoal
    }

    override fun batteryLevel(): Int = batteryLevel

    override fun checkBatteryTriggerStatus(): Boolean = batteryTriggerStatus

    override fun changeBatteryTriggerStatus(status: Boolean) {
        batteryTriggerStatus = status
    }

    override fun isInEvent(): Boolean {
        if(todaysEvents.isNullOrEmpty()) {
            return false
        }
        val currentTime = CurrentDateTime()
        for(event in todaysEvents!!) {
            if(currentTime > event.startTime!! && currentTime < event.endTime!!)
                return true
        }
        return false
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