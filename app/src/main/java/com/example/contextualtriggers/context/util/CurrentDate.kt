package com.example.contextualtriggers.context.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun CurrentDate(): String {
    return LocalDate.now().toString()
}