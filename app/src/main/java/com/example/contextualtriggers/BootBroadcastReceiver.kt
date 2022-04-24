package com.example.contextualtriggers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import com.example.contextualtriggers.triggers.GeofenceTrigger
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent


class BootBroadcastReceiver : BroadcastReceiver() {

    private val TAG = "bootcastReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("ContextTriggers", "System booted")

//        val i = Intent(context, MainActivity::class.java)
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        context.startActivity(i)

        val geofenceTrigger = GeofenceTrigger(context)

        val geofencingEvent = GeofencingEvent.fromIntent(intent)

        if (isAirplaneModeOn(context.applicationContext)) {
            geofenceTrigger.sendHighPriorityNotification(
                "Please turn off airplane Mode Airplane", "",
                MapsActivity::class.java
            )
        } else {
            if (geofencingEvent.hasError()) {
                Log.d(TAG, "onReceive: Error receiving geofence event...")
                return
            }
            val geofenceList: List<Geofence> = geofencingEvent.triggeringGeofences
            for (geofence in geofenceList) {
                Log.d(TAG, "onReceive: " + geofence.getRequestId())
            }
            val transitionType = geofencingEvent.geofenceTransition
            when (transitionType) {
                Geofence.GEOFENCE_TRANSITION_ENTER ->
                    geofenceTrigger.sendHighPriorityNotification(
                        "You have entered to your GEOFENCE", "",
                        MapsActivity::class.java
                    )
                Geofence.GEOFENCE_TRANSITION_DWELL ->
                    geofenceTrigger.sendHighPriorityNotification(
                        "GEOFENCE_TRANSITION_DWELL",
                        "",
                        MapsActivity::class.java
                    )
                Geofence.GEOFENCE_TRANSITION_EXIT ->
                    geofenceTrigger.sendHighPriorityNotification(
                        "You have exited to you GEOFENCE", "",
                        MapsActivity::class.java
                    )
            }
        }


    }

    private fun isAirplaneModeOn(context: Context): Boolean {
        return Settings.System.getInt(
            context.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON,
            0
        ) !== 0
    }
}