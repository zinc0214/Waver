package com.zinc.berrybucket.ui.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.ZoneId

fun parseDateWithZero(month: Int): String {
    return if (month > 9) "$month" else "0${month}"
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toEpochMilli(): Long {
    return this.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}