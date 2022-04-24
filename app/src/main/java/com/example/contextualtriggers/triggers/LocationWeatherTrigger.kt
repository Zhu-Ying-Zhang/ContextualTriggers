package com.example.contextualtriggers.triggers

import android.content.Intent
import com.example.contextualtriggers.context.ContextAPI
import com.example.contextualtriggers.context.util.isNightTime

private const val NOTIFICATION_TITLE = "Current location weather Trigger"
private const val NOTIFICATION_TEXT_GOOD_WEATHER = "Good weather in your current location, have a walk!"
private const val NOTIFICATION_TEXT_BAD_WEATHER = "Bad weather in your current location, maybe back to home and do some exercise!"

class LocationWeatherTrigger(
    private val contextHolder: ContextAPI
): Trigger {
    override fun getNotificationTitle(): String = NOTIFICATION_TITLE

    override fun getNotificationMessage(): String {
        return if (contextHolder.checkWeatherCodeWithLocation() >= 800)
            NOTIFICATION_TEXT_GOOD_WEATHER
        else
            NOTIFICATION_TEXT_BAD_WEATHER
    }

    override fun getNotificationIntent(): Intent? = null

    override suspend fun isTriggered(): Boolean {
        return if(contextHolder.isInEvent() || isNightTime())
            false
        else
            contextHolder.checkWeatherTriggerStatus()
    }
}