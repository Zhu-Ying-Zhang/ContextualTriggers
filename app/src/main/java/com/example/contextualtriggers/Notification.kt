package com.example.contextualtriggers

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.contextualtriggers.triggers.Trigger

class Notification {

    fun handleNotification(context: Context, trigger: Trigger) {
        handleNotification("Trigger", context, trigger)
    }

    fun handleNotification(channelId: String, context: Context, trigger: Trigger) {
        handleNotification(channelId, 101, context, trigger.getNotificationTitle(), trigger.getNotificationMessage())
    }

    fun handleNotification(channelId: String, id: Int, context: Context, trigger: Trigger) {
        handleNotification(channelId, id, context, trigger.getNotificationTitle(), trigger.getNotificationMessage())
    }

    fun handleNotification(channelId: String, id: Int, context: Context, title: String, message: String) {
        sendNotification(
            context,
            id,
            channelId,
            title,
            message
        )
    }

    // TODO: Paste notification part into this place.
    private fun sendNotification(context: Context, id: Int, channelId: String, title: String, message: String) {
        var notification: NotificationCompat.Builder =
            NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground)

        val notify: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        Log.d("TManager", "Notification Sent")
        notify.notify(id, notification.build())
    }
}