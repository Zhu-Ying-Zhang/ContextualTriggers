package com.example.contextualtriggers.triggers

import android.content.Intent
import com.example.contextualtriggers.context.ContextAPI
import com.example.contextualtriggers.context.util.isNightTime

private const val NOTIFICATION_TITLE = "Battery Status"
private const val NOTIFICATION_TEXT = "You have a lot of battery left, you should go for a walk!"

class BatteryTrigger constructor(
    private val contextHolder: ContextAPI,
): Trigger {

    override fun getNotificationTitle(): String = NOTIFICATION_TITLE

    override fun getNotificationMessage(): String = NOTIFICATION_TEXT

    override fun getNotificationIntent(): Intent? = null

    override suspend fun isTriggered(): Boolean {
        return if(contextHolder.isInEvent() || isNightTime())
            false
        else
            contextHolder.checkBatteryTriggerStatus()
    }
}