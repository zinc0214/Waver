package com.zinc.waver.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun pxToDp(px: Float): Dp {
    return with(LocalDensity.current) {
        px.toDp()
    }
}

@Composable
fun DpToPx(dp: Float): Float {
    val density = LocalDensity.current.density
    return dp * density
}
