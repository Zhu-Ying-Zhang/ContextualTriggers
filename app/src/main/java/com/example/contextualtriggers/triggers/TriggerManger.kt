package com.example.contextualtriggers.triggers

import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.contextualtriggers.Notification
import com.example.contextualtriggers.R
import com.example.contextualtriggers.context.ContextHolder

class TriggerManger constructor(
    private val context: Context,
    private val contextHolder: ContextHolder
) {

    private val noMovementTrigger = NoMovementTrigger(contextHolder = contextHolder)
    private val batteryTrigger = BatteryTrigger(contextHolder = contextHolder)

    fun check() {
        if (noMovementTrigger.isTriggered())
            Notification(context).handleNotification(noMovementTrigger)
    }
}