package com.example.contextualtriggers.context.data

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import com.example.contextualtriggers.ContextUpdateManager


class StepsData : Service(), SensorEventListener {
    private var stepsSinceBoot = 0
    private var first = true

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("ContextTrigger", "Steps service...")
        var intent = intent
        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
            Log.d("ContextTrigger", "Successfully registered")
        } else {
            Log.e("ContextTrigger", "Sensor not found...")
            intent = Intent(this, ContextUpdateManager::class.java)
            intent.putExtra("DataSource", "Steps")
            intent.putExtra("Count", -1)
            startService(intent)
            onDestroy() // stop service
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onSensorChanged(event: SensorEvent) {
        val steps = event.values[0].toInt()
        Log.d("ContextTriggers", "New steps: $steps")
        if (first) {
            stepsSinceBoot = steps
            first = false
        } else {
            val diff = steps - stepsSinceBoot
            if (diff > THRESHOLD) {
                stepsSinceBoot = steps
                val intent = Intent(this, ContextUpdateManager::class.java)
                intent.putExtra("Data", "Steps")
                intent.putExtra("Count", diff)
                startService(intent)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    companion object {
        private const val THRESHOLD = 100
    }
}
