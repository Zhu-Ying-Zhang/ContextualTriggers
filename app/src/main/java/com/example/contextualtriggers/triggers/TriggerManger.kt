package com.example.contextualtriggers.triggers

import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.contextualtriggers.R
import com.example.contextualtriggers.context.ContextHolder

class TriggerManger constructor(
    private val context: Context,
    private val contextHolder: ContextHolder
) {

    private val noMovementTrigger = NoMovementTrigger(contextHolder = contextHolder)

    fun check() {
        if (noMovementTrigger.isTriggered())
            handleNotification()
    }

    private fun handleNotification() {
        sendNotification(100,
            "TriggerNoMovement",
            noMovementTrigger.getNotificationTitle(),
            noMovementTrigger.getNotificationMessage(),
            noMovementTrigger.getNotificationIntent()
        )
    }

    // TODO: Paste notification part into this place.
    private fun sendNotification(id: Int, channelId: String, title: String, message: String, intent: Intent?) {
        var notification: NotificationCompat.Builder =
            NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground)

//        if (intent != null) {
//            notification.setContentIntent(PendingIntent.getActivity(context, 0, intent, 0))
//        }

        val notify: NotificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        Log.d("TManager", "Notification Sent")
        notify.notify(id, notification.build())
    }
}