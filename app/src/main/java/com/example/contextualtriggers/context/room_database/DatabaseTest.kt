package com.example.contextualtriggers.context.room_database

import android.os.Parcel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contextualtriggers.context.Geofence
import com.example.contextualtriggers.context.use_cases.GeofenceUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DatabaseTest @Inject constructor (
    private val geofenceUseCases: GeofenceUseCases
): ViewModel() {
    init {
        viewModelScope.launch {
            geofenceUseCases.addGeofence(
                Geofence(
                    name = "Test",
                    latitude = 50.9678443,
                    longitude = -4.45677888
                )
            )
        }
    }

    fun addGeofence() {
        viewModelScope.launch {
            geofenceUseCases.addGeofence(
                Geofence(
                    name = "Test",
                    latitude = 50.9678443,
                    longitude = -4.45677888
                )
            )
        }
    }
}