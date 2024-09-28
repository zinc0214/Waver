package com.zinc.waver.ui_detail.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class StatusInfo(
    @DrawableRes val backgroundImg: Int,
    val text: String,
    val textColor: Color
)
