package com.example.contextualtriggers

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.contextualtriggers.context.WeatherDataSource
import com.example.contextualtriggers.ui.theme.ContextualTriggersTheme
import dagger.hilt.android.AndroidEntryPoint

private const val NOTIFICATION_CHANNEL_ID_RUNNING = "Channel_Id"
private const val NOTIFICATION_CHANNEL_ID_TRIGGER = "Trigger"
private const val NOTIFICATION_CHANNEL_ID_TRIGGER_BATTERY = "Trigger_Battery"
private const val NOTIFICATION_CHANNEL_ID_ERROR = "Error"

private val REQUIRED_PERMISSIONS_LOCATION = arrayOf(
    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ContextTriggers", "Application started")
        createNotificationChannel()
        requireLocationPermissions()
        registerReceiver(getBatteryLevel, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        //This is just a test to create the geofence
         setContent {
            ContextualTriggersTheme {
                Scaffold(
                    modifier = Modifier,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(text = "Running...")

                        Button(
                            onClick = { geofenceButton()
                            },
                        ) {
                            Text(text = "Add Geofence mainactivity")
                        }

//                        DatabaseCreate()
                    }
                }
            }
        }

        //Start the ContentUpdateManager service
        val intent = Intent(this, ContextUpdateManager::class.java)
        startForegroundService(intent)

//        finish()
    }

    fun geofenceButton() {
        val mapActivity = Intent(this, MapsActivity::class.java)
        startActivity(mapActivity)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy")
        unregisterReceiver(getBatteryLevel)
        val intent = Intent(this, ContextUpdateManager::class.java)
        stopService(intent)
        val weather = Intent(this, WeatherDataSource::class.java)
        stopService(weather)
    }

    private val getBatteryLevel: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            Log.d("Battery Level", level.toString())
            val intent = Intent(context, ContextUpdateManager::class.java)
            intent.putExtra("Data", "Battery")
            intent.putExtra("batteryLevel", level)
            startService(intent)
        }
    }

    private fun createNotificationChannel() {
        val notificationChannelAppRunning =
            NotificationChannel(
                NOTIFICATION_CHANNEL_ID_RUNNING,
                NOTIFICATION_CHANNEL_ID_RUNNING,
                NotificationManager.IMPORTANCE_DEFAULT
            )

        val notificationChannelTrigger =
            NotificationChannel(
                NOTIFICATION_CHANNEL_ID_TRIGGER,
                NOTIFICATION_CHANNEL_ID_TRIGGER,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        val notificationChannelError =
            NotificationChannel(
                NOTIFICATION_CHANNEL_ID_ERROR,
                NOTIFICATION_CHANNEL_ID_ERROR,
                NotificationManager.IMPORTANCE_HIGH
            )

        val notificationChannelTriggerBattery =
            NotificationChannel(
                NOTIFICATION_CHANNEL_ID_TRIGGER_BATTERY,
                NOTIFICATION_CHANNEL_ID_TRIGGER_BATTERY,
                NotificationManager.IMPORTANCE_HIGH
            )

        getSystemService(NotificationManager::class.java)
            .createNotificationChannel(notificationChannelAppRunning)

        getSystemService(NotificationManager::class.java)
            .createNotificationChannel(notificationChannelTrigger)

        getSystemService(NotificationManager::class.java)
            .createNotificationChannel(notificationChannelError)

        getSystemService(NotificationManager::class.java)
            .createNotificationChannel(notificationChannelTriggerBattery)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("MainActivity", "into onRequestPermissionsResult $requestCode")
        when( requestCode ) {
            100  -> {
                Log.d("MainActivity", "onRequestPermissionsResult 100")
                val weather = Intent(this, WeatherDataSource::class.java)
                startService(weather)
            }
        }
    }

    private fun requireLocationPermissions() {
        val fine = ContextCompat.checkSelfPermission(applicationContext
            , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(applicationContext
            , Manifest.permission.ACCESS_COARSE_LOCATION)  == PackageManager.PERMISSION_GRANTED
        val background = ContextCompat.checkSelfPermission(applicationContext
            , Manifest.permission.ACCESS_BACKGROUND_LOCATION)  == PackageManager.PERMISSION_GRANTED

        if (!(fine && coarse && background))
            requestPermissions(
                REQUIRED_PERMISSIONS_LOCATION,
                100
            )
    }
}