package com.example.contextualtriggers.triggers

import android.content.Context
import android.util.Log
import com.example.contextualtriggers.Notification
import com.example.contextualtriggers.context.ContextState

class TriggerManger constructor(
    private val context: Context,
    private val contextState: ContextState
) {

    private val noMovementTrigger: Trigger = NoMovementTrigger(contextHolder = contextState)
    private val stepsTrigger: Trigger = StepsTrigger(contextHolder = contextState)
    private val batteryTrigger: Trigger = BatteryTrigger(contextHolder = contextState)
    private val locationWeatherTrigger: Trigger = LocationWeatherTrigger(contextHolder = contextState)
    private val weatherTrigger: Trigger = WeatherTrigger(contextHolder = contextState)

    suspend fun check() {
        if (noMovementTrigger.isTriggered())
            Notification().handleNotification("Trigger_No_Movement", 10000, context, noMovementTrigger)

        if (batteryTrigger.isTriggered()) {
            Log.d("TriggerManger", "batteryTrigger")
            Notification().handleNotification("Trigger_Battery", 10001, context, batteryTrigger)
            contextState.changeBatteryTriggerStatus(false)
        }

        if (locationWeatherTrigger.isTriggered()) {
            Notification().handleNotification("Trigger_Weather_Location", 10002, context, locationWeatherTrigger)
            contextState.updateWeatherTriggerStatus(false)
        }

        if (stepsTrigger.isTriggered())
            Notification().handleNotification("Trigger_Steps", 10003, context, stepsTrigger)

        if (weatherTrigger.isTriggered()) {
            Notification().handleNotification("Trigger_Weather", 10004, context, weatherTrigger)
            contextState.updateWeatherWithAlarmTriggerStatus(false)
        }
    }
}