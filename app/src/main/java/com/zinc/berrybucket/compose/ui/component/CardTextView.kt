package com.zinc.berrybucket.compose.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.berrybucket.compose.theme.*
import com.zinc.berrybucket.compose.ui.component.BucketCircularProgressBar
import com.zinc.berrybucket.model.BucketInfoSimple
import com.zinc.berrybucket.model.BucketProgressState
import com.zinc.berrybucket.model.BucketType

@Composable
fun CardTextView(
    itemInfo: BucketInfoSimple,
    bucketType: BucketType,
    animFinishEvent: (BucketProgressState) -> Unit,
) {

    var currentCount by rememberSaveable { mutableStateOf(itemInfo.currentCount) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        elevation = 2.dp
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
        ) {

            val (leftContent, rightContent) = createRefs()

            // Right Content = SuccessButton
            Box(
                modifier = Modifier.constrainAs(rightContent) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                contentAlignment = Alignment.CenterEnd
            ) {
                BucketCircularProgressBar(
                    progressState = animFinishEvent,
                    bucketType = BucketType.BASIC
                )
            }

            // Left Contents
            Column(
                modifier = Modifier
                    .constrainAs(leftContent) {
                        end.linkTo(rightContent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        linkTo(
                            parent.start,
                            rightContent.start,
                            startMargin = 0.dp,
                            endMargin = 10.dp,
                            bias = 0f
                        )
                    }
                    .fillMaxWidth(.8f),
                horizontalAlignment = Alignment.Start
            ) {

                // Dday
                if (itemInfo.dDay != null) {
                    DdayBadgeView(itemInfo)
                    Spacer(modifier = Modifier.height(10.dp))
                } else {
                    Spacer(modifier = Modifier.height(22.dp))
                }

                // Title
                TitleTextView(itemInfo.title)

                // Progress
                if (itemInfo.goalCount > 0) {
                    CountProgressView(
                        info = itemInfo,
                        bucketType = bucketType
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                } else {
                    Spacer(modifier = Modifier.height(22.dp))
                }


            }


        }
    }
}


@Composable
private fun DdayBadgeView(info: BucketInfoSimple) {
    Card(
        elevation = 0.dp,
        shape = RoundedCornerShape(bottomEnd = 4.dp, bottomStart = 4.dp),
        backgroundColor = info.dDayBadgeColor!!
    ) {
        Text(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 3.dp, bottom = 3.dp),
            text = info.dDayText!!,
            fontSize = 12.sp,
            color = Gray1
        )
    }
}

@Composable
private fun TitleTextView(title: String) {
    Text(
        text = title,
        color = Gray10,
        fontSize = 14.sp
    )
}

@Composable
fun CountProgressView(
    modifier: Modifier = Modifier,
    info: BucketInfoSimple,
    bucketType: BucketType
) {

    val countProgressColor =
        if (bucketType == BucketType.BASIC) {
            Main2
        } else if (info.dDay != null && info.dDay > 0) {
            Error2
        } else {
            Sub_D3
        }

    Row(modifier = modifier.padding(top = 7.dp, bottom = 7.dp)) {
        HorizontalProgressBar(
            Modifier
                .align(Alignment.CenterVertically)
                .height(8.dp),
            info,
            countProgressColor
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = info.currentCountText(),
            color = countProgressColor,
            fontSize = 13.sp
        )
        Text(
            text = "/${info.goalCountText()}",
            color = Gray4,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun HorizontalProgressBar(
    modifier: Modifier = Modifier,
    info: BucketInfoSimple,
    countProgressColor: Color
) {
    var progress by remember { mutableStateOf(0f) }
    val indicatorProgress =
        if (info.currentCount == 0) 0.0f else (info.currentCount.toFloat() / info.goalCount.toFloat())
    Log.e("ayhan", "indicatorProgress : $indicatorProgress")
    val progressAnimDuration = 1500
    val progressAnimation by animateFloatAsState(
        targetValue = indicatorProgress,
        animationSpec = tween(durationMillis = progressAnimDuration, easing = FastOutSlowInEasing)
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


@Preview(showBackground = true)
@Composable
fun Preview() {
    BaseTheme {
        CardTextView(
            itemInfo = BucketInfoSimple(
                id = "1234",
                title = "버킷리스트 아이템 버킷리스트 아이템 버킷리스트 아이템 버킷리스트 아이템 버킷리스트 아이템 버킷리스트 아이템 버킷리스트 아이템",
                currentCount = 5,
                goalCount = 10,
                dDay = 10
            ),
            BucketType.BASIC,
            {

            }
        )
    }
}