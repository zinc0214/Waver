package com.zinc.berrybucket.ui.util

fun parseDateWithZero(month: Int): String {
    return if (month > 9) "$month" else "0${month}"
}