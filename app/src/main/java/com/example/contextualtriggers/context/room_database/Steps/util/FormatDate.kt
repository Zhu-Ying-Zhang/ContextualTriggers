package com.example.contextualtriggers.context.room_database.Steps.util

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun FormatDate(dateStr: String): String {
    Log.d("TEST", dateStr)
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMM uuuu")
    val date: LocalDate = LocalDate.parse(dateStr)
    val str: String = date.format(formatter)
    Log.d("TEST", str)
    return str
}
