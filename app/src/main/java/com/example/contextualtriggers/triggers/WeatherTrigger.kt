package com.example.contextualtriggers.triggers

import android.content.Intent
import com.example.contextualtriggers.context.ContextAPI

private const val NOTIFICATION_TITLE = "Weather Trigger"
private const val NOTIFICATION_TEXT_GOOD_WEATHER = "Good weather outside, go for a walk!"
private const val NOTIFICATION_TEXT_BAD_WEATHER = "Bad weather outside, maybe stay at home and do some exercise!"


class WeatherTrigger(
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
        return false
    }
}