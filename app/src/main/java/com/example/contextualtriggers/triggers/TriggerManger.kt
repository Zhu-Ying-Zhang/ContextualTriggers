package com.example.contextualtriggers.triggers

import android.content.Context
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

    suspend fun check() {
        if (noMovementTrigger.isTriggered())
            Notification().handleNotification(context, noMovementTrigger)
        if (batteryTrigger.isTriggered()) {
            Notification().handleNotification("Trigger_Battery", 10001, context, batteryTrigger)
            contextHolder.changeBatteryTriggerStatus(false)
        }
        if (locationWeatherTrigger.isTriggered()) {
            Notification().handleNotification(context, locationWeatherTrigger)
            contextHolder.updateWeatherTriggerStatus(false)
        }
//        if (stepsTrigger.isTriggered())
//            Notification().handleNotification(context, stepsTrigger)
    }
}