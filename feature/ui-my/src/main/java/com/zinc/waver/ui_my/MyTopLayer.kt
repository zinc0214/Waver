package com.zinc.waver.ui_my

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zinc.common.models.TopProfile
import com.zinc.waver.ui.design.theme.Gray9
import com.zinc.waver.ui.presentation.component.IconButton
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.presentation.component.ProfileLayer
import com.zinc.waver.ui.presentation.screen.blank.MyTopLayerLoading
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_my.model.MyTopEvent
import com.zinc.waver.util.pxToDp

@Composable
fun MyTopLayer(
    profileInfo: TopProfile?,
    layoutChanged: (Dp) -> Unit,
    myTopEvent: (MyTopEvent) -> Unit
) {
    val context = LocalContext.current
    val density = context.resources.displayMetrics.density

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned {
                val heightInDp = pxToDp(it.size.height.toFloat(), density) - 95.dp
                Log.e("ayhan", "size height ${it.size.height}, $heightInDp")
                layoutChanged(heightInDp)
            }) {

        TopButtonLayer(
            modifier = Modifier
                .padding(top = 16.dp, end = 16.dp)
                .align(Alignment.End),
            alarmClicked = {
                myTopEvent(MyTopEvent.Alarm)
            }
        )

        if (profileInfo == null) {
            Spacer(modifier = Modifier.height(22.dp))
            MyTopLayerLoading()
        } else {
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
@Preview
private fun ProfileLayerPreview() {
    Box() {

        MyTopLayer(
            profileInfo = TopProfile(
                name = "한아라고해",
                imgUrl = null,
                percent = 0.0f,
                badgeImgUrl = null,
                badgeTitle = "딩가딩가딩 딩가링가링",
                bio = "안녕, 나를 한 아 라고 불러줘",
                followerCount = "10",
                followingCount = "20"
            ), layoutChanged = {}
        ) {}

        MyTopLayer(
            profileInfo = null, layoutChanged = { }, {})
    }
}

@Composable
@Preview
private fun MyTopLayerPreview() {

}