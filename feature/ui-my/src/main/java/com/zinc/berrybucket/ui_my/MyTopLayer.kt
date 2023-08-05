package com.zinc.berrybucket.ui_my

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Gray7
import com.zinc.berrybucket.ui.design.theme.Gray9
import com.zinc.berrybucket.ui.design.theme.Main3
import com.zinc.berrybucket.ui.presentation.component.IconButton
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.presentation.component.ProfileCircularProgressBar
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_my.model.MyTopEvent
import com.zinc.domain.models.TopProfile

@Composable
fun MyTopLayer(
    profileInfo: TopProfile?,
    myTopEvent: (MyTopEvent) -> Unit
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
                    .align(Alignment.End),
                alarmClicked = {
                    myTopEvent(MyTopEvent.Alarm)
                }
            )

            Spacer(modifier = Modifier.height(4.dp))
            ProfileLayer(profileInfo)

            Spacer(modifier = Modifier.height(24.dp))
            FollowStateLayer(
                profileInfo,
                Modifier.align(Alignment.CenterHorizontally)
            ) { myTopEvent.invoke(it) }
        }
    }
}

@Composable
private fun TopButtonLayer(modifier: Modifier, alarmClicked: () -> Unit) {
    Row(modifier = modifier) {

        IconButton(
            modifier = Modifier
                .then(Modifier.size(32.dp)),
            onClick = {
                alarmClicked()
            },
            image = R.drawable.alarm,
            contentDescription = stringResource(R.string.alarm)
        )
    }
}

@Composable
private fun ProfileLayer(profileInfo: TopProfile) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            ProfileCircularProgressBar(
                percentage = profileInfo.percent,
                profileImageUrl = profileInfo.imgUrl ?: ""
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        MyText(
            modifier = Modifier
                .padding(start = 26.dp, end = 26.dp)
                .align(Alignment.CenterHorizontally),
            text = profileInfo.badgeTitle ?: "",
            fontSize = dpToSp(15.dp),
            color = Main3,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        MyText(
            modifier = Modifier
                .padding(start = 26.dp, end = 26.dp)
                .align(Alignment.CenterHorizontally),
            text = profileInfo.name,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
            fontSize = dpToSp(20.dp),
            color = Gray10,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        MyText(
            modifier = Modifier
                .padding(start = 26.dp, end = 26.dp)
                .align(Alignment.CenterHorizontally),
            text = profileInfo.bio ?: "",
            fontSize = dpToSp(14.dp),
            color = Gray7,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FollowStateLayer(
    topProfile: TopProfile,
    modifier: Modifier,
    followClicked: (MyTopEvent) -> Unit
) {
    Row(modifier = modifier) {
        FollowStateView(
            modifier = Modifier.align(Alignment.CenterVertically),
            topProfile.followerCount,
            stringResource(id = R.string.followerText)
        ) { followClicked(MyTopEvent.Follower) }
        Spacer(modifier = Modifier.width(70.dp))
        FollowStateView(
            modifier = Modifier.align(Alignment.CenterVertically),
            topProfile.followingCount, stringResource(id = R.string.followingText)
        ) { followClicked(MyTopEvent.Following) }
    }
}

@Composable
private fun FollowStateView(
    modifier: Modifier,
    count: String,
    text: String,
    followClicked: () -> Unit
) {
    Row(modifier = modifier.clickable { followClicked() }) {
        MyText(
            text = text,
            fontSize = dpToSp(13.dp),
            color = Gray9,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(8.dp))
        MyText(
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
        stringResource(id = com.zinc.berrybucket.ui_common.R.string.allTab),
        stringResource(id = com.zinc.berrybucket.ui_common.R.string.categoryTab),
        stringResource(id = com.zinc.berrybucket.ui_common.R.string.ddayTab),
        stringResource(id = com.zinc.berrybucket.ui_common.R.string.challengeTab)
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
                MyText(text = title)
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

@Composable
@Preview
private fun ProfileLayerPreview() {
    ProfileLayer(
        profileInfo = TopProfile(
            name = "한아라고해",
            imgUrl = null,
            percent = 0.0f,
            badgeType = null,
            badgeTitle = "딩가딩가딩 딩가링가링",
            bio = "안녕, 나를 한 아 라고 불러줘",
            followerCount = "10",
            followingCount = "20"
        )
    )
}