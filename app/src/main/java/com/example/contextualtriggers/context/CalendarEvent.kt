package com.example.contextualtriggers.context

import android.os.Parcel
import android.os.Parcelable

class CalendarEvent private constructor(`in`: Parcel) : Parcelable {
    val title: String? = `in`.readString()
    val startTime: String? = `in`.readString()
    val endTime: String? = `in`.readString()

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(title)
        parcel.writeString(startTime)
        parcel.writeString(endTime)
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
