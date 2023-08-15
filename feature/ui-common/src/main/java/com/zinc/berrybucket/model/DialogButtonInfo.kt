package com.zinc.berrybucket.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color

data class DialogButtonInfo(
    @StringRes val text: Int,
    val color: Color
)