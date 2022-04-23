package com.example.contextualtriggers.context

import android.content.Context
import android.util.Log
import com.example.contextualtriggers.context.util.CurrentDate
import com.example.contextualtriggers.context.use_cases.Geofence.GeofenceUseCases
import com.example.contextualtriggers.context.use_cases.Steps.StepsUseCases
import com.example.contextualtriggers.context.util.CurrentDateTime

class ContextHolder constructor(
    context: Context,
    private val geofenceUseCases: GeofenceUseCases,
    private val stepsUseCases: StepsUseCases
): ContextAPI {

    var batteryLevel: Int = 0
    private var context: Context = context
    var noMovement = false
    private var batteryTriggerStatus = true
    var todaysEvents: ArrayList<CalendarEvent>? = null

    override fun noMovement(): Boolean = noMovement

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
}