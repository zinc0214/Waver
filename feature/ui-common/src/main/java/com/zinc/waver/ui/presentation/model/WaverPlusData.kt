package com.zinc.waver.ui.presentation.model

import androidx.compose.ui.graphics.painter.Painter

enum class WaverPlusType {
    YEAR, MONTH
}

data class WaverPlusOption(
    val imgResource: Painter,
    val title: String,
    val content: String
)

data class WaverPlusBottomOption(
    val type: WaverPlusType,
    val title: String,
    val content: String,
    val subContent: String
)