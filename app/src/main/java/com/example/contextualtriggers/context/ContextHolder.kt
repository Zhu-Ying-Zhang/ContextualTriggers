package com.example.contextualtriggers.context

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
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
    var batteryTriggerStatus = true

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
}