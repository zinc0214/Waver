package com.zinc.waver.ui.presentation.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zinc.waver.ui.design.theme.Gray3

@Composable
fun HorizontalProgressBar(
    modifier: Modifier = Modifier,
    _currentCount: Int,
    _goalCount: Int,
    _countProgressColor: Color
) {
    var currentCount by remember { mutableIntStateOf(_currentCount) }
    var goalCount by remember { mutableIntStateOf(_goalCount) }
    var countProgressColor by remember { mutableStateOf(_countProgressColor) }

    LaunchedEffect(_currentCount) {
        currentCount = _currentCount
    }

    LaunchedEffect(_goalCount) {
        goalCount = _goalCount
    }

    LaunchedEffect(_countProgressColor) {
        countProgressColor = _countProgressColor
    }

    var progress by remember { mutableStateOf(0f) }
    val indicatorProgress =
        if (currentCount == 0) 0.0f else (currentCount.toFloat() / goalCount.toFloat())
    val progressAnimDuration = 1500
    val progressAnimation by animateFloatAsState(
        targetValue = indicatorProgress,
        animationSpec = tween(durationMillis = progressAnimDuration, easing = FastOutSlowInEasing),
        label = ""
    )
    LinearProgressIndicator(
        modifier = modifier
            .width(176.dp)
            .height(8.dp)
            .clip(RoundedCornerShape(2.dp)), // Rounded edges
        progress = progressAnimation,
        color = countProgressColor,
        backgroundColor = Gray3
    )
    LaunchedEffect(indicatorProgress) {
        progress = indicatorProgress
    }
}

