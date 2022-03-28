package com.example.contextualtriggers.triggers

import android.content.Intent

interface Trigger {

    fun getNotificationTitle(): String

    fun getNotificationMessage(): String

    fun getNotificationIntent(): Intent?

    fun isTriggered(): Boolean
}