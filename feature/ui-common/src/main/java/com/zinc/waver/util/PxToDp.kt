package com.zinc.waver.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity

@Composable
fun PxToDp(px: Float): Float {
    val density = LocalDensity.current.density
    return px / density
}

@Composable
fun DpToPx(dp: Float): Float {
    val density = LocalDensity.current.density
    return dp * density
}
