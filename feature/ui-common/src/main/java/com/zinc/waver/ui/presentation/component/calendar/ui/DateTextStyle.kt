package com.zinc.waver.ui.presentation.component.calendar.ui

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray8

object DateTextStyle {
    val selected = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        color = Gray1
    )

    val unSelected = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 15.sp,
        color = Gray8
    )
}
