package com.example.contextualtriggers.context

import android.os.Parcel
import android.os.Parcelable

class Geofences(
    val name: String?,
    val latitude: Double,
    val longitude: Double
): Parcelable {

    var id: Long = Long.MAX_VALUE

    fun getID(): Long
    {
        return id
    }

    fun setID(id: Long) {
        this.id = id
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeLong(Id)
        parcel.writeString(name)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Geofences> {
        override fun createFromParcel(parcel: Parcel): Geofences {
            return Geofences(parcel)
        }

        override fun newArray(size: Int): Array<Geofences?> {
            return arrayOfNulls(size)
        }
    }
}