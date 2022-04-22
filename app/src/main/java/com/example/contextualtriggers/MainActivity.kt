package com.example.contextualtriggers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.contextualtriggers.ui.theme.ContextualTriggersTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var batteryTrigger = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ContextTriggers", "Application started")

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

    override fun onResume() {
        super.onResume()
        registerReceiver(getBatteryLevel, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    private val getBatteryLevel: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            if (level >= 60) {
                if (!batteryTrigger) {
                    Log.d("Battery Level", level.toString())
                    Notification(context).handleNotification(
                        "Battery Status",
                        "You have a lot of battery left, you can go for a walk!"
                    )
                    batteryTrigger = true
                }
            } else
                batteryTrigger = false
        }
    }
}