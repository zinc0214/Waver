package com.zinc.berrybucket.ui.presentation.my

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zinc.berrybucket.R
import com.zinc.berrybucket.ui.compose.theme.*
import com.zinc.berrybucket.ui.presentation.common.IconButton
import com.zinc.berrybucket.ui.presentation.common.ProfileCircularProgressBar
import com.zinc.domain.models.TopProfile

@Composable
fun MyTopLayer(
    profileInfo: TopProfile?
) {

    profileInfo?.let {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            TopButtonLayer(
                modifier = Modifier
                    .padding(top = 16.dp, end = 16.dp)
                    .align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(4.dp))
            ProfileLayer(profileInfo)

            Spacer(modifier = Modifier.height(24.dp))
            FollowStateLayer(profileInfo, Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

@Composable
private fun TopButtonLayer(modifier: Modifier) {
    Row(modifier = modifier) {

        IconButton(
            modifier = Modifier
                .then(Modifier.size(32.dp)),
            onClick = {
                /*TODO*/
            },
            image = R.drawable.alarm,
            contentDescription = stringResource(R.string.alarm)
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            modifier = Modifier
                .then(Modifier.size(32.dp)),
            onClick = {
                /*TODO*/
            },
            image = R.drawable.more,
            contentDescription = stringResource(R.string.more)
        )
    }
}

@Composable
private fun ProfileLayer(profileInfo: TopProfile) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            ProfileCircularProgressBar(
                percentage = profileInfo.percent,
                profileImageUrl = profileInfo.profileImg
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier
                .padding(start = 26.dp, end = 26.dp)
                .align(Alignment.CenterHorizontally),
            text = profileInfo.titlePosition,
            fontSize = 15.sp,
            color = Main3,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier
                .padding(start = 26.dp, end = 26.dp)
                .align(Alignment.CenterHorizontally),
            text = profileInfo.nickName,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Gray10,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier
                .padding(start = 26.dp, end = 26.dp)
                .align(Alignment.CenterHorizontally),
            text = profileInfo.bio,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Gray7,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FollowStateLayer(topProfile: TopProfile, modifier: Modifier) {
    Row(modifier = modifier) {
        FollowStateView(
            modifier = Modifier.align(Alignment.CenterVertically),
            topProfile.followerCount,
            stringResource(id = R.string.followerText)
        )
        Spacer(modifier = Modifier.width(70.dp))
        FollowStateView(
            modifier = Modifier.align(Alignment.CenterVertically),
            topProfile.followingCount, stringResource(id = R.string.followingText)
        )
    }
}

@Composable
private fun FollowStateView(modifier: Modifier, count: String, text: String) {
    Row(modifier = modifier) {
        Text(
            text = text,
            fontSize = 13.sp,
            color = Gray9,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = count,
            fontSize = 15.sp,
            color = Gray9,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
private fun TabLayer() {
    val tabData = listOf(
        stringResource(id = R.string.allTab),
        stringResource(id = R.string.categoryTab),
        stringResource(id = R.string.ddayTab),
        stringResource(id = R.string.challengeTab)
    )

    Tabs(tabData)
}

@Composable
fun Tabs(tabs: List<String>) {
    var tabIndex by remember { mutableStateOf(0) }

    TabRow(
        selectedTabIndex = tabIndex,
        backgroundColor = Gray1,
        contentColor = Gray10,
        divider = {
            TabRowDefaults.Divider(
                thickness = 3.dp,
                color = Gray1
            )
        },
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.customTabIndicatorOffset(tabPositions[tabIndex]),
                height = 3.dp,
                color = Gray10
            )
        }
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(selected = tabIndex == index, onClick = {
                tabIndex = index
            }, text = {
                Text(text = title)
            })
        }
    }
}

fun Modifier.customTabIndicatorOffset(
    currentTabPosition: TabPosition
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "tabIndicatorOffset"
        value = currentTabPosition
    }
) {
    val indicatorWidth = 32.dp
    val currentTabWidth = currentTabPosition.width
    val indicatorOffset by animateDpAsState(
        targetValue = currentTabPosition.left + currentTabWidth / 2 - indicatorWidth / 2,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(indicatorWidth)
}