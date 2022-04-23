package com.example.contextualtriggers

import android.Manifest.permission.READ_CALENDAR
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.contextualtriggers.context.room_database.DatabaseCreate
import com.example.contextualtriggers.ui.theme.ContextualTriggersTheme
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var batteryTrigger = false
    private val permission: String = android.Manifest.permission.READ_CALENDAR
    private val requestCode: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ContextTriggers", "Application started")
        if (ContextCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        } else {
            readCalendarEvents()
        }
    }

    private fun readCalendarEvents()
    {
        val titleCol = CalendarContract.Events.TITLE
        val startDateCol = CalendarContract.Events.DTSTART
        val endDateCol = CalendarContract.Events.DTEND

        val projection = arrayOf(titleCol, startDateCol, endDateCol)
        val selection = CalendarContract.Events.IS_PRIMARY + " == 1"

        val cursor = contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            projection, selection, null, null
        )

        val titleColIdx = cursor!!.getColumnIndex(titleCol)
        val startDateColIdx = cursor.getColumnIndex(startDateCol)
        val endDateColIdx = cursor.getColumnIndex(endDateCol)

        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)

        while (cursor.moveToNext()) {
            val title = cursor.getString(titleColIdx)
            val startDate = formatter.format(Date(cursor.getLong(startDateColIdx)))
            val endDate = formatter.format(Date(cursor.getLong(endDateColIdx)))

            if(startDate.subSequence(0, 10).contentEquals(CurrentDate().substring(0, 10))) {
                Log.d("Upcoming Events", "$startDate ${CurrentDate()}")
                Log.d("Upcoming Events", "$title $startDate $endDate")
            }
        }

        cursor.close()


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

                        DatabaseCreate()
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun CurrentDate(): String {
        return LocalDate.now().toString()
    }
}