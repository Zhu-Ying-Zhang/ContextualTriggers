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

    private var context: Context = context
    var noMovement = true

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

    override fun getBatteryLevel(): Int {
        var filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        var batteryStatus: Intent? = context.registerReceiver(null, filter)

        var batteryLevel = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val batteryScale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

        val batteryProportion: Float = (batteryLevel!! / batteryScale!!.toFloat()) * 100
        println("BATTERY LEVEL: $batteryProportion")
        return batteryProportion.toInt()
    }
}