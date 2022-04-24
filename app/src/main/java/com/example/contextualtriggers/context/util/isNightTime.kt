package com.example.contextualtriggers.context.util

import android.util.Log
import java.time.LocalTime

fun isNightTime(): Boolean {
    val currentTime = LocalTime.now().toString().substring(0, 5)
//    Log.d("Time", "$currentTime")
    val latest = "22:00"
    val earliest = "06:00"
    return (currentTime < earliest) || (currentTime > latest)
}