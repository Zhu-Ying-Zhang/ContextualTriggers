package com.example.contextualtriggers

import android.app.*
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.provider.CalendarContract
import android.util.Log
import com.example.contextualtriggers.context.*
import com.example.contextualtriggers.context.room_database.Geofence.GeofenceDatabase
import com.example.contextualtriggers.context.room_database.Geofence.GeofenceRepoImplementation
import com.example.contextualtriggers.context.room_database.Steps.StepsDatabase
import com.example.contextualtriggers.context.room_database.Steps.StepsRepoImplementation
import com.example.contextualtriggers.context.util.CurrentDate
import com.example.contextualtriggers.context.use_cases.Geofence.AddGeofence
import com.example.contextualtriggers.context.use_cases.Geofence.GeofenceUseCases
import com.example.contextualtriggers.context.use_cases.Geofence.GetGeofence
import com.example.contextualtriggers.context.use_cases.Steps.*
import com.example.contextualtriggers.context.util.isNightTime
import com.example.contextualtriggers.triggers.Trigger
import com.example.contextualtriggers.triggers.TriggerManger
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

private const val NOTIFICATION_ID = 1001
private const val NOTIFICATION_CHANNEL_ID = "Channel_Id"

class ContextUpdateManager: Service() {

    private lateinit var contextHolder: ContextHolder
    private lateinit var triggerManager: TriggerManger

    override fun onCreate() {
        Log.d("ContextTrigger", "Creating context manager...")
        super.onCreate()
        startForeground(
            NOTIFICATION_ID, sendNotification(
                "Service is running", "Service enabled"
            ).build()
        )
        val geofenceDatabase = GeofenceDatabase.getInstance(this)
        val geofenceRepository = GeofenceRepoImplementation(geofenceDatabase.geofenceDao)
        val geofenceUseCases = GeofenceUseCases(
            addGeofence = AddGeofence(geofenceRepository),
            getGeofence = GetGeofence(geofenceRepository)
        )
        val stepsDatabase = StepsDatabase.getInstance(this)
        val stepsRepository = StepsRepoImplementation(stepsDatabase.stepsDao)
        val stepsUseCases = StepsUseCases(
            addSteps = AddSteps(stepsRepository),
            getSteps = GetSteps(stepsRepository),
            insertSteps = InsertSteps(stepsRepository),
            stepsExist = StepsExist(stepsRepository)
        )
        contextHolder = ContextHolder(this, geofenceUseCases, stepsUseCases)
        triggerManager = TriggerManger(this, contextHolder)

        val stepCounter = Intent(this, StepsData::class.java)
        startService(stepCounter)

        val calendarData = Intent(this, CalendarData::class.java)
        startService(calendarData)
        val cal = Calendar.getInstance()
        val pendingCalendar = PendingIntent.getService(this, 0, calendarData, PendingIntent.FLAG_IMMUTABLE)
        val alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        Log.d("Main", java.lang.String.valueOf(cal.timeInMillis))
        //21600000
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis, 1000, pendingCalendar)

        val weatherData = Intent(this, WeatherData::class.java)
        val pendingWeather = PendingIntent.getService(this, 1, weatherData, PendingIntent.FLAG_IMMUTABLE)
        //21600000
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis, 1000, pendingWeather)
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
                Log.d("ContextUpdate - type", type!!)
                if(type == "Steps") {
                    val steps = intent.getIntExtra("Count", 0);
                    if (steps != -1) {
                        GlobalScope.launch {
                            contextHolder.addSteps(steps)
                        }
                        print("Updated steps")
                    }
                } else if (type == "Battery") {
                    val level = intent.getIntExtra("batteryLevel", 0)
                    Log.d("ContextUpdate", level.toString())
                    contextHolder.batteryLevel = level
                    contextHolder.changeBatteryTriggerStatus(true)
                } else if (type == "Calendar") {
                    val events = intent.getParcelableArrayListExtra<CalendarEvent>("Events")
                    Log.d("ContextUpdate", "Events Updating")
                    contextHolder.todaysEvents = events
                    contextHolder.nextEvent()
                    val test = contextHolder.isInEvent()
                    val test2 = isNightTime()
                    Log.d("ContextUpdate", "Event: $test, Night: $test2")
                } else if (type == "WeatherWithLocation") {
                    val weatherCode = intent.getIntExtra("WeatherCode", 0)
                    Log.d("ContextUpdate", "WeatherWithLocation")
                    contextHolder.updateWeatherCodeWithLocation(weatherCode)
                    contextHolder.updateWeatherTriggerStatus(true)
                } else if (type == "WeatherWithAlarm") {
                    val weatherCode = intent.getIntExtra("WeatherCode", 0)
                    Log.d("ContextUpdate", "WeatherWithAlarm")
                    contextHolder.updateWeatherCodeWithAlarm(weatherCode)
                    contextHolder.updateWeatherWithAlarmTriggerStatus(true)
                }
            }
            if (triggerManager != null)
                GlobalScope.launch {
                    Log.d("triggerManager-Launch", "Checking..")
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

    private fun readCalendarEvents() {
        Log.d("Upcoming Events", "Reading Calendar Data")
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

            if (startDate.subSequence(0, 10).contentEquals(CurrentDate().substring(0, 10))) {
                Log.d("Upcoming Events", "$title $startDate $endDate")
            }
        }

        cursor.close()
    }
}