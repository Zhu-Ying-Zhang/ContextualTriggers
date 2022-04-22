package com.example.contextualtriggers.context

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Pair
import com.example.contextualtriggers.triggers.atWork

class ContextHolder constructor(
    context: Context
//    private var location: Pair<Double?, Double?>? = null

): ContextAPI {

    private var context: Context = context
    var noMovement = true
    var atwork = false
//    override fun getLocation(): Pair<Double?, Double?>? {
//        return getLocation();
//    }

    override fun noMovement(): Boolean = noMovement
    override fun atwork(): Boolean = atwork

    override fun getBatteryLevel(): Int {
        var filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        var batteryStatus: Intent? = context.registerReceiver(null, filter)

        if (batteryStatus != null) {

            var batteryLevel = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val batteryScale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

            val batteryProportion: Float = (batteryLevel!! / batteryScale!!.toFloat()) * 100
            println("BATTERY LEVEL: $batteryProportion")
            return batteryProportion.toInt()
        }else {
            return -1;
        }

        }
    }
