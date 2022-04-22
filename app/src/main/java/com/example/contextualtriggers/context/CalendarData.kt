package com.example.contextualtriggers.context

import android.Manifest
import android.app.Service
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.Parcel
import android.provider.CalendarContract
import android.util.Pair
import androidx.core.app.ActivityCompat
import com.example.contextualtriggers.ContextUpdateManager
import java.util.*


class CalendarData : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    fun onHandleIntent(intent: Intent?) {
        var intent = intent
        val cr = contentResolver
        val period = todayMillis
        var results: ArrayList<CalendarEvent?>? = null
        val read = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED
        if (read) {
            val builder = CalendarContract.Instances.CONTENT_URI.buildUpon()
            ContentUris.appendId(builder, period.first)
            ContentUris.appendId(builder, period.second)
            val cursor = cr.query(builder.build(), INSTANCES_PROJECTION, null, null, null)
            if (cursor != null) {
                results = ArrayList<CalendarEvent?>()
                while (cursor.moveToNext()) {
                    val title = cursor.getString(INSTANCE_TITLE_INDEX)
                    val location = cursor.getString(INSTANCE_EVENT_LOCATION_INDEX)
                    val begin = cursor.getLong(INSTANCE_BEGIN_INDEX)
                    val parcel = Parcel.obtain()
                    parcel.writeString(title)
                    parcel.writeString(location)
                    parcel.writeLong(begin)
                    parcel.setDataPosition(0)
                    val event: CalendarEvent = CalendarEvent.createFromParcel(parcel)
                    results!!.add(event)
                    parcel.recycle()
                }
                cursor.close()
            }
        }
        intent = Intent(this, ContextUpdateManager::class.java)
        intent.putExtra("Data", "Calendar")
        intent.putParcelableArrayListExtra("CalendarEvent", results)
        startService(intent)
    }

    private val todayMillis: Pair<Long, Long>
        private get() {
            val now = System.currentTimeMillis()
            val today = Calendar.getInstance()
            today[Calendar.HOUR_OF_DAY] = 23
            today[Calendar.MINUTE] = 59
            today[Calendar.SECOND] = 59
            today[Calendar.MILLISECOND] = 999
            return Pair(now, today.timeInMillis)
        }

    companion object {
        const val TAG = "calendar"
        val INSTANCES_PROJECTION = arrayOf(
            CalendarContract.Instances.TITLE,
            CalendarContract.Instances.EVENT_LOCATION,
            CalendarContract.Instances.BEGIN
        )
        private const val INSTANCE_TITLE_INDEX = 0
        private const val INSTANCE_EVENT_LOCATION_INDEX = 1
        private const val INSTANCE_BEGIN_INDEX = 2
    }
}
