package com.zinc.waver.ui.presentation.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui_common.R

@Composable
fun ProfileCircularProgressBar(
    percentage: Float,
    radius: Dp = 43.dp,
    color: Color = Main4,
    animDuration: Int = 1000,
    animDelay: Int = 0,
    profileImageUrl: String,
    badgeImageUrl: String
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercentage = animateFloatAsState(
        targetValue = if (animationPlayed) percentage else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = animDelay
        ), label = "curPercentage"
    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Box(
        contentAlignment = Alignment.Center
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

        BadgeImage(badgeImageUrl)
    }
}

@Composable
fun ProfileCenterImage(profileImageUrl: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(profileImageUrl)
            .crossfade(true)
            .build(),
        error = painterResource(R.drawable.testimg),
        placeholder = painterResource(R.drawable.testimg),
        contentDescription = stringResource(R.string.profileImageDesc),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(3.dp)
            .height(80.dp)
            .width(80.dp)
            .clip(shape = CircleShape)
    )
}

@Composable
fun BadgeImage(url: String) {
//    val angle = (-90f) + 360 * curPercentage
//    val radian = angle * 0.0175
//    val x = radius * cos(radian)
//    val y = radius * sin(radian)
//

    Image(
        painter = rememberAsyncImagePainter(
            model = url,
            error = painterResource(id = R.drawable.badge_small)
        ),
        contentDescription = null,
        modifier = Modifier
            .padding(top = 80.dp)
            .width(36.dp)
            .height(36.dp)
        //.offset(x = x.dp, y = y.dp)

    )
}
