package com.zinc.berrybucket.ui.presentation.common.calendar.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle

data class CalendarDate(
    val dateInMilli: Long,
    val backgroundColour: Color = Color.Unspecified,
    val backgroundShape: Shape,
    val textStyle: TextStyle? = null
)
