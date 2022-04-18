package com.example.contextualtriggers

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.contextualtriggers.triggers.Trigger

class Notification constructor(private val context: Context) {

    fun handleNotification(title: String, message: String) {
        sendNotification(100,
            "Channel_Id",
            title,
            message,
            null
        )
    }

    fun handleNotification(trigger: Trigger) {
        sendNotification(100,
            "Channel_Id",
            trigger.getNotificationTitle(),
            trigger.getNotificationMessage(),
            trigger.getNotificationIntent()
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

        val notify: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        Log.d("TManager", "Notification Sent")
        notify.notify(id, notification.build())
    }
}