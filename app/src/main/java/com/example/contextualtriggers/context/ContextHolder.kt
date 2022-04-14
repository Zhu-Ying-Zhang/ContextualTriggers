package com.example.contextualtriggers.context

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

class ContextHolder constructor(
    context: Context
): ContextAPI {

    private var context: Context = context
    var noMovement = false

    override fun noMovement(): Boolean = noMovement

    override fun getBatteryLevel(): Int {
        var filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        var batteryStatus: Intent? = context.registerReceiver(null, filter)

        var batteryLevel = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val batteryScale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

        val batteryProportion: Float = (batteryLevel!! / batteryScale!!.toFloat()) * 100
        println("BATTERY LEVEL: $batteryProportion")
        return batteryProportion.toInt()
    }
}