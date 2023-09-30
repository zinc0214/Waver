package com.zinc.berrybucket.ui_my

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
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
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.berrybucket.model.BucketProgressState
import com.zinc.berrybucket.model.MyTabType
import com.zinc.berrybucket.model.MyTabType.ALL
import com.zinc.berrybucket.model.UIBucketInfoSimple
import com.zinc.berrybucket.ui.design.theme.Error2
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Gray3
import com.zinc.berrybucket.ui.design.theme.Gray4
import com.zinc.berrybucket.ui.design.theme.Gray5
import com.zinc.berrybucket.ui.design.theme.Gray6
import com.zinc.berrybucket.ui.design.theme.Main2
import com.zinc.berrybucket.ui.design.theme.Sub_D2
import com.zinc.berrybucket.ui.design.theme.Sub_D3
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_my.view.BucketCircularProgressBar
import com.zinc.berrybucket.util.shadow

@Composable
fun SimpleBucketListView(
    bucketList: List<UIBucketInfoSimple>,
    tabType: MyTabType,
    showDday: Boolean,
    itemClicked: (UIBucketInfoSimple) -> Unit,
    achieveClicked: (String) -> Unit,
    nestedScrollInterop: NestedScrollConnection? = null
) {

    var data by remember { mutableStateOf(bucketList) }

    LaunchedEffect(bucketList) {
        data = bucketList
    }

    val columModifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)

    if (nestedScrollInterop != null) {
        columModifier.nestedScroll(nestedScrollInterop)
    }

    LazyColumn(
        modifier = columModifier,
        contentPadding = PaddingValues(bottom = 140.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        itemsIndexed(data, key = { _, item -> item.id }) { _, bucket ->
            SimpleBucketCard(
                itemInfo = bucket,
                tabType = tabType,
                isShowDday = showDday,
                itemClicked = { itemClicked.invoke(it) },
                achieveClicked = { achieveClicked.invoke(it) }
            )
        }
    }
}

@Composable
fun SimpleBucketCard(
    itemInfo: UIBucketInfoSimple,
    tabType: MyTabType,
    isShowDday: Boolean,
    itemClicked: (UIBucketInfoSimple) -> Unit,
    achieveClicked: (String) -> Unit
) {

    var bucket by remember { mutableStateOf(itemInfo) }

    LaunchedEffect(itemInfo) {
        bucket = itemInfo
    }

    var bucketCount = remember { mutableStateOf(bucket.currentCount) }
    val borderColor = remember { mutableStateOf(Color.Transparent) }
    val isProgress = remember { mutableStateOf(bucket.isProgress()) }
    val backgroundColor = remember { mutableStateOf(if (isProgress.value) Gray1 else Gray3) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isProgress.value) borderColor.value else Color.Transparent,
                shape = RoundedCornerShape(4.dp)
            )
            .shadow(
                color = Gray5.copy(alpha = 0.2f),
                offsetX = (0).dp,
                offsetY = (0).dp,
                blurRadius = 4.dp,
            )
            .clip(RoundedCornerShape(4.dp))
            .clickable { itemClicked(itemInfo) }
            .background(
                color = backgroundColor.value,
                shape = RoundedCornerShape(4.dp)
            ),
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
        ) {

            val (leftContent, rightContent) = createRefs()

            // Right Content = SuccessButton
            if (isProgress.value) {
                Card(
                    shape = CircleShape,
                    elevation = 0.dp,
                    modifier = Modifier
                        .constrainAs(rightContent) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                ) {
                    BucketCircularProgressBar(
                        progressState = {
                            Log.e("ayhan", "state : $it , $bucketCount, ${itemInfo.goalCount}")
                            if (it == BucketProgressState.PROGRESS_END) {
                                if (tabType is ALL) {
                                    borderColor.value = Main2
                                } else {
                                    borderColor.value = Sub_D2
                                }
                                bucketCount.value += 1
                            } else if (it == BucketProgressState.FINISHED) {
                                borderColor.value = Color.Transparent
                                if (itemInfo.goalCount <= bucketCount.value) {
                                    isProgress.value = false
                                    backgroundColor.value = Gray3
                                }
                                achieveClicked(itemInfo.id)
                            }
                        },
                        tabType = tabType
                    )
                }
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
                if (bucket.dDay != null && isProgress.value && isShowDday) {
                    DdayBadgeView(bucket)
                    Spacer(modifier = Modifier.height(10.dp))
                } else {
                    Spacer(modifier = Modifier.height(22.dp))
                }

                // Title
                TitleTextView(bucket.title, isProgress.value)

                // Progress
                if (bucket.goalCount > 0 && isProgress.value) {
                    CountProgressView(
                        info = bucket,
                        bucketCount = bucketCount.value,
                        tabType = tabType
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
private fun DdayBadgeView(info: UIBucketInfoSimple) {
    Card(
        elevation = 0.dp,
        shape = RoundedCornerShape(bottomEnd = 4.dp, bottomStart = 4.dp),
        backgroundColor = info.dDayBadgeColor!!
    ) {
        MyText(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 3.dp, bottom = 3.dp),
            text = info.dDayText!!,
            fontSize = dpToSp(12.dp),
            color = Gray1
        )
    }
}

@Composable
private fun TitleTextView(title: String, progress: Boolean) {
    MyText(
        text = title,
        color = if (progress) Gray10 else Gray6,
        fontSize = dpToSp(14.dp),
    )
}

@Composable
private fun CountProgressView(
    modifier: Modifier = Modifier,
    info: UIBucketInfoSimple,
    bucketCount: Int,
    tabType: MyTabType
) {

    val countProgressColor =
        if (tabType is ALL) {
            Main2
        } else if (info.dDay != null && info.dDay!! > 0) {
            Error2
        } else {
            Sub_D3
        }

    Row(modifier = modifier.padding(top = 7.dp, bottom = 7.dp)) {
        HorizontalProgressBar(
            Modifier
                .align(Alignment.CenterVertically)
                .height(8.dp),
            bucketCount,
            info.goalCount,
            countProgressColor
        )
        Spacer(modifier = Modifier.width(12.dp))
        MyText(
            text = bucketCount.toString(),
            color = countProgressColor,
            fontSize = dpToSp(13.dp),
        )
        MyText(
            text = "/${info.goalCountText()}",
            color = Gray4,
            fontSize = dpToSp(13.dp),
        )
    }
}

@Composable
fun HorizontalProgressBar(
    modifier: Modifier = Modifier,
    currentCount: Int,
    goalCount: Int,
    countProgressColor: Color
) {
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

