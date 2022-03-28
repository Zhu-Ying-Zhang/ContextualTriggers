package com.example.contextualtriggers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.contextualtriggers.context.ContextHolder
import com.example.contextualtriggers.triggers.TriggerManger
import java.time.LocalDateTime

private const val NOTIFICATION_ID = 1001
private const val NOTIFICATION_CHANNEL_ID = "Channel_Id"

class ContextUpdateManager: Service() {

    private lateinit var contextHolder: ContextHolder
    private lateinit var triggerManager: TriggerManger

    override fun onCreate() {
        super.onCreate()
        contextHolder = ContextHolder()
        triggerManager = TriggerManger(this, contextHolder)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        startForeground()
        return START_STICKY
    }

    private fun startForeground(){
        var count = 0
        Thread(
            Runnable {
                kotlin.run {
                    while (true) {
                        count++
                        if (count % 5 == 0) {
                            contextHolder?.noMovement = true
                            if (triggerManager != null) {
                                triggerManager.check()
                            }
                        }
                        Log.e("Service", "${LocalDateTime.now()} Service is running....")
                        try {
                            Thread.sleep(10000)
                        } catch (e:Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        ).start()

        startForeground(
            NOTIFICATION_ID, sendNotification(
                "Service is running", "Service enabled"
            ).build()
        )
    }

    private fun sendNotification(title: String, message: String): Notification.Builder {
        val notificationChannel =
            NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_ID,
                NotificationManager.IMPORTANCE_DEFAULT
            )

        getSystemService(NotificationManager::class.java)
            .createNotificationChannel(notificationChannel)

        return Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentText(title)
            .setContentTitle(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
    }
}