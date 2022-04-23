package com.example.contextualtriggers.triggers

import android.content.Intent
import android.util.Log
import com.example.contextualtriggers.context.ContextAPI

private const val NOTIFICATION_TITLE = "Battery Status"
private const val NOTIFICATION_TEXT = "You have a lot of battery left, you can go for a walk!"

class BatteryTrigger constructor(
    private val contextHolder: ContextAPI,
    private val batteryTarget: Int = 60
): Trigger {

    override fun getNotificationTitle(): String = NOTIFICATION_TITLE

    override fun getNotificationMessage(): String = NOTIFICATION_TEXT

    override fun getNotificationIntent(): Intent? = null

    override suspend fun isTriggered(): Boolean {
        val batteryProportion: Int = contextHolder.batteryLevel()
        Log.d("BatteryTrigger-1", batteryProportion.toString())
        if (batteryProportion >= batteryTarget)
            Log.d("BatteryTrigger-2", contextHolder.checkBatteryTriggerStatus().toString())
            if (contextHolder.checkBatteryTriggerStatus()) {
                Log.d("BatteryTrigger-3", contextHolder.checkBatteryTriggerStatus().toString())
                contextHolder.changeBatteryTriggerStatus(false)
                return true
            }
        else {
                Log.d("BatteryTrigger-4", contextHolder.checkBatteryTriggerStatus().toString())
                contextHolder.changeBatteryTriggerStatus(true)
                Log.d("BatteryTrigger-5", contextHolder.checkBatteryTriggerStatus().toString())
                return false
            }
    }
}