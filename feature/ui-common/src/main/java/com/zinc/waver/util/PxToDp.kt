package com.zinc.waver.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun pxToDp(px: Float): Dp {
    return with(LocalDensity.current) {
        px.toDp()
    }
}

fun pxToDp(px: Float, density: Float): Dp {
    return (px / density).dp
}

@Composable
fun DpToPx(dp: Float): Float {
    val density = LocalDensity.current.density
    return dp * density
}
