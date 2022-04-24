package com.example.contextualtriggers.context.util

import java.time.LocalTime

fun CurrentTime(): String {
    return LocalTime.now().toString().substring(0, 5)
}