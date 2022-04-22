package com.example.contextualtriggers.context


import android.util.Pair
import com.example.contextualtriggers.triggers.atWork


interface ContextAPI {

//    fun getLocation(): Pair<Double?, Double?>?
    fun noMovement(): Boolean
    fun getBatteryLevel(): Int
    fun atwork(): Boolean
}