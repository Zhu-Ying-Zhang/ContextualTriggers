package com.example.contextualtriggers.context.util

import java.time.LocalDateTime

fun CurrentDateTime() : String {
    return LocalDateTime.now().toString()
}
