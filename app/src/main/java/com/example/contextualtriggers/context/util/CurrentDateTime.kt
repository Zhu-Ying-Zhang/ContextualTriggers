package com.example.contextualtriggers.context.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun CurrentDateTime() : String {
    val dateTime = LocalDateTime.now()
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm")
    return dateTime.format(formatter)
}
