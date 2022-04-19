package com.example.contextualtriggers

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.example.contextualtriggers.context.ContextHolder
import com.example.contextualtriggers.context.StepsData
import com.example.contextualtriggers.triggers.TriggerManger

private const val NOTIFICATION_ID = 1001
private const val NOTIFICATION_CHANNEL_ID = "Channel_Id"

class ContextUpdateManager: Service() {

    private lateinit var contextHolder: ContextHolder
    private lateinit var triggerManager: TriggerManger

    override fun onCreate() {
        Log.e("ContextTrigger", "Creating context manager...")
        super.onCreate()
        contextHolder = ContextHolder(this)
        triggerManager = TriggerManger(this, contextHolder)
//        if(ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
//            //ask for permission
//            requestPermissions( {Manifest.permission.ACTIVITY_RECOGNITION}.toString(), PHYISCAL_ACTIVITY)
//        }
        val stepCounter = Intent(this, StepsData::class.java)
        startService(stepCounter)
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
        if (triggerManager != null)
            triggerManager.check()
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