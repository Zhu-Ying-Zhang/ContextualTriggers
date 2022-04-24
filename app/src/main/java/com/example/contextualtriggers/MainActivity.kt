package com.example.contextualtriggers

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.contextualtriggers.context.util.StepsInputField
import com.example.contextualtriggers.context.WeatherLocationData
import com.example.contextualtriggers.ui.theme.ContextualTriggersTheme
import com.tbruyelle.rxpermissions3.RxPermissions
import dagger.hilt.android.AndroidEntryPoint

private const val NOTIFICATION_CHANNEL_ID_RUNNING = "Channel_Id"
private const val NOTIFICATION_CHANNEL_ID_TRIGGER = "Trigger"
private const val NOTIFICATION_CHANNEL_ID_TRIGGER_BATTERY = "Trigger_Battery"
private const val NOTIFICATION_CHANNEL_ID_ERROR = "Error"

private val REQUIRED_PERMISSIONS = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.READ_CALENDAR,
)

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    var sendIntent = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ContextTriggers", "Application started")
        createNotificationChannel()
        requirePermissions()
        registerReceiver(getBatteryLevel, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        // Start the ContentUpdateManager service
        val intent = Intent(this, ContextUpdateManager::class.java)
        startForegroundService(intent)

        // Setup screen
         setContent {
            ContextualTriggersTheme {
                createSetupScreen()
            }
        }

//        finish()
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun createSetupScreen() {
        val keyboardController = LocalSoftwareKeyboardController.current
        val inputState = remember{
            mutableStateOf("")
        }
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        Scaffold(
            modifier = Modifier,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Column(
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Text(
                            text = "Initial Setup",
                            style = MaterialTheme.typography.h5,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }
                Card(
                    modifier = Modifier
                        .padding(12.dp)
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = 3.dp
                ) {
                    Column{
                        Box(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(text = "Please set locations for your Home, Work & Gym " +
                                    "(optional).\n\n" +
                                    "This will allow for improved notifications!")
                        }
                        Spacer(Modifier.height(8.dp))
                        Button(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            onClick = { geofenceButton()
                            },
                        ) {
                            Text(text = "Add Significant Locations")
                        }
                        Spacer(Modifier.height(24.dp))
                    }
                }
                Spacer(Modifier.height(0.dp))
                Card(
                    modifier = Modifier
                        .padding(12.dp)
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = 3.dp
                ) {
                    Column{
                        Box(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(text = "Set your daily steps goal here:")
                        }
                        Box(
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .padding()
                        ) {
                            StepsInputField(
                                modifier = Modifier.focusRequester(focusRequester),
                                value = inputState.value,
                                onValueChange = {
                                    inputState.value = it
                                },
                                labelId = "Enter steps",
                                enabled = true,
                                isSingleLine = true,
                                onAction = KeyboardActions() {

                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                            )
                        }
                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
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
        val weather = Intent(this, WeatherLocationData::class.java)
        stopService(weather)
    }

    private val getBatteryLevel: BroadcastReceiver = object : BroadcastReceiver() {

        var previousCharge = 0

        override fun onReceive(context: Context, intent: Intent) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
//            if(abs(level - previousCharge) < 10) {
//                return
//            }
//            else {
//                previousCharge = level
//            }
            if (level >= 60 && sendIntent) {
                Log.d("Battery Level", level.toString())
                sendIntent = false
                val intent = Intent(context, ContextUpdateManager::class.java)
                intent.putExtra("Data", "Battery")
                intent.putExtra("batteryLevel", level)
                startService(intent)
            } else if (level < 60) {
                Log.d("Battery Level", "Not send intent")
                sendIntent = true
            }
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
        Log.d("MainActivity", permissions.toList().toString())
        Log.d("MainActivity", grantResults.toList().toString())
        if (permissions.contentEquals(REQUIRED_PERMISSIONS)) {
            if (Manifest.permission.ACCESS_FINE_LOCATION in permissions){
                if (grantResults[permissions.indexOf(Manifest.permission.ACCESS_FINE_LOCATION)] == 0) {
//                    val weather = Intent(this, WeatherData::class.java)
//                    startService(weather)
                }
            }
        }
    }

    private fun requirePermissions() {
        RxPermissions(this)
            .requestEachCombined(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CALENDAR
            )
            .subscribe { permission ->
                if (permission.granted) {
                    Log.d("requirePermissions", "permission.granted")
                    val weather = Intent(this, WeatherLocationData::class.java)
                    startService(weather)
                }
                if (!permission.granted) {
                    ActivityCompat.requestPermissions(
                        this,
                        REQUIRED_PERMISSIONS,
                        100
                    )
                }
            }
    }
}