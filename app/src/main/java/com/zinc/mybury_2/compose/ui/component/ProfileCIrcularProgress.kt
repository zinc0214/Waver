package com.zinc.mybury_2.compose.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import com.zinc.mybury_2.compose.theme.Main4
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun ProfileCircularProgressBar(
        percentage: Float,
        radius: Dp = 43.dp,
        color: Color = Main4,
        animDuration: Int = 1000,
        animDelay: Int = 0,
        profileImageUrl: String
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

    Box(contentAlignment = Alignment.Center, modifier = Modifier
            .size(radius * 3)
    ) {
        ProfileCenterImage(profileImageUrl)

        Canvas(modifier = Modifier.size(radius * 2f)) {
            drawArc(
                    color = color,
                    -90f,
                    360 * curPercentage.value,
                    useCenter = false,
                    style = Stroke(3.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        BadgeImage(radius.value, curPercentage.value)

    }

}

@Composable
fun ProfileCenterImage(profileImageUrl: String) {
//    GlideImage(
//        imageModel = profileImageUrl,
//        contentScale = ContentScale.Crop,
//        placeHolder = ImageBitmap.imageResource(R.drawable.kakao),
//        error = ImageBitmap.imageResource(R.drawable.kakao),
//        requestOptions = RequestOptions()
//            .override(80, 80)
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .centerCrop(),
//    )
    Image(
            painterResource(R.drawable.kakao),
            contentDescription = null,
            modifier = Modifier
                    .padding(3.dp)
                    .height(80.dp)
                    .width(80.dp)
                    .clip(shape = CircleShape),
            contentScale = ContentScale.Crop
    )
}

@Composable
fun BadgeImage(radius: Float, curPercentage: Float) {
    val angle = (-90f) + 360 * curPercentage
    val radian = angle * 0.0175
    val x = radius * cos(radian)
    val y = radius * sin(radian)

    Image(
            painterResource(R.drawable.badge),
            contentDescription = null,
            modifier = Modifier
                    .width(32.dp)
                    .height(32.dp)
                    .offset(x = x.dp, y = y.dp)

    )
}
