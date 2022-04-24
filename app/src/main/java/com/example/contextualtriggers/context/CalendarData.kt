package com.example.contextualtriggers.context

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Parcel
import android.provider.CalendarContract
import android.util.Log
import com.example.contextualtriggers.ContextUpdateManager
import com.example.contextualtriggers.context.util.CurrentDate
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarData: Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d("Upcoming Events", "Starting calendar service...")
        readCalendarEvents()
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun readCalendarEvents() {
        Log.d("Upcoming Events", "Reading Calendar Data")
        val titleCol = CalendarContract.Events.TITLE
        val startDateCol = CalendarContract.Events.DTSTART
        val endDateCol = CalendarContract.Events.DTEND

        val projection = arrayOf(titleCol, startDateCol, endDateCol)
        val selection = CalendarContract.Events.IS_PRIMARY + " == 1"

        val cursor = contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            projection, selection, null, null
        )

        val titleColIdx = cursor!!.getColumnIndex(titleCol)
        val startDateColIdx = cursor.getColumnIndex(startDateCol)
        val endDateColIdx = cursor.getColumnIndex(endDateCol)

        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
        var events = ArrayList<CalendarEvent>()

        while (cursor.moveToNext()) {
            val title = cursor.getString(titleColIdx)
            val startDate = formatter.format(Date(cursor.getLong(startDateColIdx)))
            val endDate = formatter.format(Date(cursor.getLong(endDateColIdx)))

            if (startDate.subSequence(0, 10).contentEquals(CurrentDate().substring(0, 10))) {
                Log.d("Upcoming Events", "$title $startDate $endDate")
                val parcel = Parcel.obtain()
                parcel.writeString(title)
                parcel.writeString(startDate.toString())
                parcel.writeString(endDate.toString())
                parcel.setDataPosition(0)
                val event = CalendarEvent.createFromParcel(parcel)
                events.add(event)
                parcel.recycle()
            }
        }
        val intent = Intent(this, ContextUpdateManager::class.java)
        intent.putExtra("Data", "Calendar")
        intent.putParcelableArrayListExtra("Events", events)
        startService(intent)
        cursor.close()
    }
}
