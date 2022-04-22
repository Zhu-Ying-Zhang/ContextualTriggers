package com.example.contextualtriggers.triggers

import android.content.Intent
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
        val batteryProportion: Int = contextHolder.getBatteryLevel()
        return batteryProportion >= batteryTarget
    }
}