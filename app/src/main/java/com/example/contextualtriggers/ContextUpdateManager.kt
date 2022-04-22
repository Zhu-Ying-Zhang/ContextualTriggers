package com.example.contextualtriggers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.example.contextualtriggers.context.ContextHolder
import com.example.contextualtriggers.context.StepsData
import com.example.contextualtriggers.context.use_cases.Geofence.GeofenceUseCases
import com.example.contextualtriggers.context.use_cases.Steps.StepsUseCases
import com.example.contextualtriggers.triggers.TriggerManger
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val NOTIFICATION_ID = 1001
private const val NOTIFICATION_CHANNEL_ID = "Channel_Id"

class ContextUpdateManager(): Service() {

    @Inject
    lateinit var stepsUseCases: StepsUseCases
    @Inject
    lateinit var geofenceUseCases: GeofenceUseCases

    private lateinit var contextHolder: ContextHolder
    private lateinit var triggerManager: TriggerManger

    override fun onCreate() {
        Log.d("ContextTrigger", "Creating context manager...")
        super.onCreate()
        contextHolder = ContextHolder(this, geofenceUseCases, stepsUseCases)
        triggerManager = TriggerManger(this, contextHolder)
        val stepCounter = Intent(this, StepsData::class.java)
        startService(stepCounter)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        var bundle: Bundle? = if(intent != null) {
            intent.extras!!;
        }else{
            null;
        }
        if (bundle != null && intent != null) {
            if (intent.hasExtra("Data")) {
                // saving data received from datasource
                val type = intent.getStringExtra("Data")
                if(type == "Steps") {
                    val steps = intent.getIntExtra("Count", 0);
                    if (steps != -1) {
                        GlobalScope.launch {
                            contextHolder.addSteps(steps)
                        }
                        print("Updated steps")
                    }
                }
            }

        }
        startForeground()
        return START_STICKY
    }

    private fun startForeground() {
        if (triggerManager != null)
            GlobalScope.launch {
                triggerManager.check()
            }
        startForeground(
            NOTIFICATION_ID, sendNotification(
                "Service is running", "Service enabled"
            ).build()
        )
    }

    override fun onDestroy() {
        super.onDestroy()
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