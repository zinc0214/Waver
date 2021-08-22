package com.zinc.mybury_2.compose.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zinc.mybury_2.R
import com.zinc.mybury_2.compose.theme.Main3

@Composable
fun BucketCircularProgressBar(
    radius: Dp = 32.dp,
    color: Color = Main3,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercentage = animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = animDelay
        )
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
                .size(radius * 2)
                .clickable {
                    animationPlayed = true

                }
                .padding(3.5.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.check),
            contentDescription = null,
            modifier = Modifier
                    .height(32.dp)
                    .width(32.dp)
        )

        Canvas(modifier = Modifier.size(radius * 2f)) {
            drawArc(
                color = color,
                -90f,
                360 * curPercentage.value,
                useCenter = false,
                style = Stroke(2.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        if ((curPercentage.value * 100).toInt() == 100) {
            Image(
                painter = painterResource(R.drawable.check_succeed),
                contentDescription = null,
                modifier = Modifier
                        .height(32.dp)
                        .width(32.dp)
                        .clip(shape = CircleShape),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Composable
fun CircularProgressBar(
    percentage: Float,
    radius: Dp = 27.dp,
    color: Color = Main3,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercentage = animateFloatAsState(
        targetValue = if (animationPlayed) percentage else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = animDelay
        )
    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(radius * 2)
    ) {
        Canvas(modifier = Modifier.size(radius * 2f)) {
            drawArc(
                color = color,
                -90f,
                360 * curPercentage.value,
                useCenter = false,
                style = Stroke(5.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Image(
            painterResource(R.drawable.appicon_m),
            contentDescription = null,
            modifier = Modifier
                    .width(15.dp)
                    .height(15.dp)

        )

        Image(
            painterResource(R.drawable.kakao),
            contentDescription = null,
            modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
                    .clip(shape = CircleShape),
            contentScale = ContentScale.Crop
        )
        /*Text(
            text = (curPercentage.value * number).toInt().toString(),
            color = Color.Black,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )*/
    }
}