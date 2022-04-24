package com.example.contextualtriggers.context

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.IBinder
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.contextualtriggers.ContextUpdateManager
import com.example.contextualtriggers.Notification
import org.json.JSONObject

private const val TAG = "WeatherData"

class WeatherData: Service() {

    private var weatherUrl = ""
    private var apiId = "084075e7553a4b0f967ed5dbe97be187"

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand..")
        super.onStartCommand(intent, flags, startId)
        val intent = Intent(this, ContextUpdateManager::class.java)
        LocationHelper().startListeningUserLocation(
            this, object : MyLocationListener {
                override fun onLocationChanged(location: Location?) {
                    WeatherLocationData.mLocation = location
                    WeatherLocationData.mLocation?.let {
                        Log.d(TAG, "WeatherData --> mLocation")
                        weatherUrl = "https://api.weatherbit.io/v2.0/current?lat="+ location?.latitude +"&lon=" + location?.longitude + "&key="+ apiId
                        Log.d(TAG, weatherUrl)
                        getWeatherData(intent)
                    }
                }
            })
        return START_STICKY
    }

    private fun getWeatherData(intent: Intent) {
        val queue = Volley.newRequestQueue(this)
        val url: String = weatherUrl
        val stringReq = StringRequest(
            Request.Method.GET, url,
            { response ->
                Log.d(TAG, response.toString())
                val obj = JSONObject(response)
                val data = obj.getJSONArray("data").getJSONObject(0).getJSONObject("weather")
                Log.d(TAG, data.toString())
                intent.putExtra("Data", "WeatherWithAlarm")
                intent.putExtra("WeatherCode", data.getInt("code"))
                startService(intent)
            }, {
                Log.d(TAG, "Error...")
                Notification().handleNotification(
                    "Error",
                    100,
                    this,
                    "Important Message",
                    "Please check your network status!")
            }
        )
        queue.add(stringReq)
    }

    companion object {
        var mLocation: Location? = null
    }

}