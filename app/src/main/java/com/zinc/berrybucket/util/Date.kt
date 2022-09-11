package com.zinc.berrybucket.util

import java.time.LocalDate

fun LocalDate.parseWithDday(): String {
    val year = this.year
    val month = parseMonth(this.month.value)
    val date = this.dayOfMonth

    val calDday = this.compareTo(LocalDate.now())

    return "${year}.${month}.${date}(D-${calDday})"
}

fun parseMonth(month: Int): String {
    return if (month > 9) "$month" else "0${month}"
}
