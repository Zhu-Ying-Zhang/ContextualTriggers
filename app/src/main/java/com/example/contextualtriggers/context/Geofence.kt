package com.example.contextualtriggers.context

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "geofence_tbl")
class Geofence(
    val name: String?,
    val latitude: Double,
    val longitude: Double,

    @PrimaryKey
    val id: Int? = null,
) {
//: Parcelable {
//
//    constructor(parcel: Parcel) : this(
//        parcel.readString(),
//        parcel.readDouble(),
//        parcel.readDouble(),
//        parcel.readInt()
//    )
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(name)
//        parcel.writeDouble(latitude)
//        parcel.writeDouble(longitude)
//        if (id != null) {
//            parcel.writeInt(id)
//        }
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<Geofence> {
//        override fun createFromParcel(parcel: Parcel): Geofence {
//            return Geofence(parcel)
//        }
//
//        override fun newArray(size: Int): Array<Geofence?> {
//            return arrayOfNulls(size)
//        }
//    }
}