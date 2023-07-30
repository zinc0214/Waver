package com.zinc.berrybucket.ui.presentation.detail.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class StatusInfo(
    @DrawableRes val backgroundImg: Int,
    val text: String,
    val textColor: Color
)
