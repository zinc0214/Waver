package com.zinc.berrybucket.ui.presentation.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray4

@Composable
fun SwitchOnlyView(
    modifier: Modifier = Modifier,
    scale: Float = 1f,
    width: Dp = 50.dp,
    height: Dp = 30.dp,
    strokeWidth: Dp = 0.dp,
    uncheckedTrackColor: Color = Gray4,
    gapBetweenThumbAndTrackEdge: Dp = 3.dp
) {

    val thumbRadius = (height / 2) - gapBetweenThumbAndTrackEdge

    // To move thumb, we need to calculate the position (along x axis)
    val animatePosition = animateFloatAsState(
        targetValue = with(LocalDensity.current) { (thumbRadius + gapBetweenThumbAndTrackEdge).toPx() }
    )

    Canvas(
        modifier = modifier
            .size(width = width, height = height)
            .scale(scale = scale)
            .background(
                color = uncheckedTrackColor,
                shape = RoundedCornerShape(15.dp)
            )
    ) {
        // Track
        drawRoundRect(
            color = uncheckedTrackColor,
            cornerRadius = CornerRadius(x = 15.dp.toPx(), y = 15.dp.toPx()),
            style = Stroke(width = strokeWidth.toPx())
        )

        // Thumb
        drawCircle(
            color = Gray1,
            radius = thumbRadius.toPx(),
            center = Offset(
                x = animatePosition.value,
                y = size.height / 2
            )
        )
    }
}