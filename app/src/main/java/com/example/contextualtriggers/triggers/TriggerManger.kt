package com.example.contextualtriggers.triggers

import android.content.Context
import android.util.Log
import com.example.contextualtriggers.Notification
import com.example.contextualtriggers.context.ContextHolder

class TriggerManger constructor(
    private val context: Context,
    private val contextHolder: ContextHolder
) {

    private val noMovementTrigger: Trigger = NoMovementTrigger(contextHolder = contextHolder)
    private val stepsTrigger: Trigger = StepsTrigger(contextHolder = contextHolder)
    private val batteryTrigger: Trigger = BatteryTrigger(contextHolder = contextHolder)
    private val locationWeatherTrigger: Trigger = LocationWeatherTrigger(contextHolder = contextHolder)
    private val weatherTrigger: Trigger = WeatherTrigger(contextHolder = contextHolder)

    suspend fun check() {
        if (noMovementTrigger.isTriggered())
            Notification().handleNotification("Trigger_No_Movement", 10000, context, noMovementTrigger)

        if (batteryTrigger.isTriggered()) {
            Log.d("TriggerManger", "batteryTrigger")
            Notification().handleNotification("Trigger_Battery", 10001, context, batteryTrigger)
            contextHolder.changeBatteryTriggerStatus(false)
        }

        if (locationWeatherTrigger.isTriggered()) {
            Notification().handleNotification("Trigger_Weather_Location", 10002, context, locationWeatherTrigger)
            contextHolder.updateWeatherTriggerStatus(false)
        }

        if (stepsTrigger.isTriggered())
            Notification().handleNotification("Trigger_Steps", 10003, context, stepsTrigger)

        if (weatherTrigger.isTriggered()) {
            Notification().handleNotification("Trigger_Weather", 10004, context, weatherTrigger)
            contextHolder.updateWeatherWithAlarmTriggerStatus(false)
        }
    }
}