package com.zinc.berrybucket.ui_my

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Gray7
import com.zinc.berrybucket.ui.design.theme.Gray9
import com.zinc.berrybucket.ui.design.theme.Main3
import com.zinc.berrybucket.ui.presentation.common.IconButton
import com.zinc.berrybucket.ui.presentation.common.ProfileCircularProgressBar
import com.zinc.berrybucket.ui.util.dpToSp
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
            fontSize = dpToSp(15.dp),
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
            fontSize = dpToSp(20.dp),
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
            fontSize = dpToSp(14.dp),
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
            fontSize = dpToSp(13.dp),
            color = Gray9,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = count,
            fontSize = dpToSp(15.dp),
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