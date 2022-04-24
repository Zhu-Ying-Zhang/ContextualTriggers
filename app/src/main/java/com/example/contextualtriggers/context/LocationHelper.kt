package com.example.contextualtriggers.context

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat

// 1 min(Will change to 1 hour). The Minimum Time to get location update
private const val LOCATION_REFRESH_TIME = 60000
// 500 meters. The Minimum Distance to be changed to get location update
private const val LOCATION_REFRESH_DISTANCE = 1000

// 1 min(Will change to 1 hour). The Minimum Time to get location update
private const val LOCATION_REFRESH_TIME_AND_REMOVE = 3000
// 500 meters. The Minimum Distance to be changed to get location update
private const val LOCATION_REFRESH_DISTANCE_AND_REMOVE  = 0

class LocationHelper {

    @SuppressLint("MissingPermission")
    fun startListeningUserLocation(context: Context, locationListener: MyLocationListener) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        Log.d("LocationHelper", "startListeningUserLocation")
        val locationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // calling listener to inform that updated location is available
                locationListener.onLocationChanged(location)
                Log.d("LocationHelper", "onLocationChanged")
            }

            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            LOCATION_REFRESH_TIME.toLong(),
            LOCATION_REFRESH_DISTANCE.toFloat(),
            locationListener
        )

    }

    @SuppressLint("MissingPermission")
    fun updateAndRemoveListeningUserLocation(context: Context, locationListener: MyLocationListener) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        Log.d("LocationHelper", "startListeningUserLocation")
        val locationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // calling listener to inform that updated location is available
                locationListener.onLocationChanged(location)
                Log.d("LocationHelper", "onLocationChanged")
            }

            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            LOCATION_REFRESH_TIME_AND_REMOVE.toLong(),
            LOCATION_REFRESH_DISTANCE_AND_REMOVE.toFloat(),
            locationListener
        )
        locationManager.removeUpdates(locationListener)
    }
}

interface MyLocationListener {
    fun onLocationChanged(location: Location?)
}