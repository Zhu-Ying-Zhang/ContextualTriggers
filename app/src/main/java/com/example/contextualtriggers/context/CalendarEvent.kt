package com.example.contextualtriggers.context

import android.os.Parcel
import android.os.Parcelable

class CalendarEvent private constructor(`in`: Parcel) : Parcelable {
    private val title: String? = `in`.readString()
    private val location: String? = `in`.readString()
    private val startTime: Long = `in`.readLong()

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(title)
        parcel.writeString(location)
        parcel.writeLong(startTime)
    }

    companion object CREATOR : Parcelable.Creator<CalendarEvent> {
        override fun createFromParcel(parcel: Parcel): CalendarEvent {
            return CalendarEvent(parcel)
        }

        override fun newArray(size: Int): Array<CalendarEvent?> {
            return arrayOfNulls(size)
        }
    }
}
