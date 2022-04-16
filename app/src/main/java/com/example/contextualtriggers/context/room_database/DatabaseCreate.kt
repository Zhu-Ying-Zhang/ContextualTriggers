package com.example.contextualtriggers.context.room_database

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun DatabaseCreate(
    viewModel: DatabaseTest = hiltViewModel()

) {
    Button(
        onClick = { viewModel.addGeofence()
                 },
    ) {
        Text(text = "Add Geofence")
    }
}