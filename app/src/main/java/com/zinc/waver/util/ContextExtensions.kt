package com.zinc.waver.util

import android.content.Context
import android.util.TypedValue

fun <T : Number> Context?.dp2px(dp: T, default: Int = 0) =
    if (this == null) default else TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        this.resources.displayMetrics
    ).toInt()