package com.zinc.berrybucket.ui_other.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Gray4
import com.zinc.berrybucket.ui.design.theme.Gray7
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.presentation.component.ProfileLayer
import com.zinc.berrybucket.ui.presentation.component.TitleIconType
import com.zinc.berrybucket.ui.presentation.component.TitleView
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_other.R
import com.zinc.domain.models.TopProfile

@Composable
fun OtherHomeProfile(
    profileInfo: TopProfile,
    isAlreadyFollowed: Boolean,
    changeFollowStatus: (Boolean) -> Unit,
    goToBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TitleView(
            title = "",
            leftIconType = TitleIconType.BACK,
            isDividerVisible = false,
            onLeftIconClicked = {
                goToBack()
            }
        )
        ProfileLayer(profileInfo)
        Divider(color = Gray4, thickness = 0.5.dp, modifier = Modifier.padding(top = 20.dp))
        OtherProfileStatus(
            profileInfo.followerCount,
            profileInfo.followingCount,
            isAlreadyFollowed
        ) {
            changeFollowStatus(it)
        }
    }
}

@Composable
private fun OtherProfileStatus(
    followerCount: String,
    followingCount: String,
    isAlreadyFollowed: Boolean,
    changeFollowStatus: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        FollowCountTextView(
            modifier = Modifier.weight(3f),
            stringResource(id = R.string.followerText),
            followerCount
        )
        FollowCountTextView(
            modifier = Modifier.weight(3f),
            stringResource(id = R.string.followingText), followingCount
        )
        FollowedStatusButton(
            modifier = Modifier.weight(4f),
            isAlreadyFollowed, changeFollowStatus
        )
    }
}

@Composable
private fun FollowCountTextView(modifier: Modifier, text: String, number: String) {
    Row(
        modifier = modifier
            .wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        MyText(
            text = text,
            color = Gray10,
            fontSize = dpToSp(dp = 15.dp),
            modifier = Modifier.padding(end = 4.dp)
        )
        MyText(text = number, color = Gray10, fontSize = dpToSp(dp = 17.dp))
    }
}

@Composable
private fun FollowedStatusButton(
    modifier: Modifier,
    isAlreadyFollowed: Boolean,
    changeFollowStatus: (Boolean) -> Unit
) {
    val text =
        if (isAlreadyFollowed) stringResource(id = R.string.otherFollowed)
        else stringResource(id = R.string.otherFollow)
    val bgColor = if (isAlreadyFollowed) Gray7 else Main4
    Row(
        modifier = modifier
            .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        MyText(text = text, modifier = modifier.background(color = bgColor), color = Gray1)
    }
}

@Composable
@Preview
private fun OtherHomeProfilePreview() {
    OtherHomeProfile(
        profileInfo = TopProfile(
            name = "안녕다른사람",
            imgUrl = null,
            percent = 0.3f,
            badgeType = null,
            badgeTitle = "신나는여행자",
            bio = "나는다른사람이라고해요",
            followerCount = "10",
            followingCount = "20"
        ),
        false,
        {}, {}
    )
}