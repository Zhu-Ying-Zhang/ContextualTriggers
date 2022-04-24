package com.example.contextualtriggers.triggers

import android.content.Intent
import com.example.contextualtriggers.context.ContextAPI

private const val NOTIFICATION_TITLE = "Weather Trigger"
private const val NOTIFICATION_TEXT_GOOD_WEATHER = "It's good weather now, go for a walk!"
private const val NOTIFICATION_TEXT_BAD_WEATHER = "It's bad weather now, maybe stay at home and do some exercise!"


class WeatherTrigger(
    private val contextHolder: ContextAPI
): Trigger {
    override fun getNotificationTitle(): String = NOTIFICATION_TITLE

    override fun getNotificationMessage(): String {
        return if (contextHolder.checkWeatherCodeWithAlarm() >= 800) {
            NOTIFICATION_TEXT_GOOD_WEATHER
        } else {
            NOTIFICATION_TEXT_BAD_WEATHER
        }
    }

    override fun getNotificationIntent(): Intent? = null

    override suspend fun isTriggered(): Boolean = contextHolder.checkWeatherWithAlarmTriggerStatus()
}