package com.example.contextualtriggers

import android.app.*
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.room.Room
import com.example.contextualtriggers.context.ContextHolder
import com.example.contextualtriggers.context.StepsData
import com.example.contextualtriggers.context.room_database.Steps.StepsDatabase
import com.example.contextualtriggers.context.room_database.Steps.StepsRepoImplementation
import com.example.contextualtriggers.context.use_cases.Geofence.GeofenceUseCases
import com.example.contextualtriggers.context.use_cases.Steps.*
import com.example.contextualtriggers.triggers.TriggerManger
import dagger.hilt.android.internal.Contexts
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val NOTIFICATION_ID = 1001
private const val NOTIFICATION_CHANNEL_ID = "Channel_Id"

class ContextUpdateManager(): Service() {

//    @Inject
//    lateinit var geofenceUseCases: GeofenceUseCases

    private lateinit var contextHolder: ContextHolder
    private lateinit var triggerManager: TriggerManger

    override fun onCreate() {
        Log.d("ContextTrigger", "Creating context manager...")
        super.onCreate()
        val stepsDatabase = StepsDatabase.getInstance(this)
        val stepsRepository = StepsRepoImplementation(stepsDatabase.stepsDao)
        val stepsUseCases = StepsUseCases(
            addSteps = AddSteps(stepsRepository),
            getSteps = GetSteps(stepsRepository),
            insertSteps = InsertSteps(stepsRepository),
            stepsExist = StepsExist(stepsRepository)
        )
        contextHolder = ContextHolder(this, stepsUseCases)
        triggerManager = TriggerManger(this, contextHolder)
        val stepCounter = Intent(this, StepsData::class.java)
        startService(stepCounter)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        var bundle: Bundle? = intent?.extras

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
            if (triggerManager != null)
                GlobalScope.launch {
                    triggerManager.check()
                }
        }
        else {
            startForeground()
        }

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