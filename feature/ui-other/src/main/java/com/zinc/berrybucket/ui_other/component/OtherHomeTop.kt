package com.zinc.berrybucket.ui_other.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.ui.design.theme.Gray2
import com.zinc.berrybucket.ui.design.theme.Gray5
import com.zinc.berrybucket.ui.design.theme.Gray7
import com.zinc.berrybucket.ui.design.theme.Gray9
import com.zinc.berrybucket.ui.design.theme.Main1
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.presentation.component.ProfileLayer
import com.zinc.berrybucket.ui.presentation.component.TitleIconType
import com.zinc.berrybucket.ui.presentation.component.TitleView
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_other.R
import com.zinc.common.models.TopProfile

@Composable
fun OtherHomeProfile(
    profileInfo: TopProfile,
    changeFollowStatus: (Boolean) -> Unit,
    goToBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TitleView(
            title = "",
            leftIconType = TitleIconType.BACK,
            isDividerVisible = false,
            onLeftIconClicked = {
                goToBack()
            }
        )
        ProfileLayer(profileInfo)
        FollowStatus(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            profileInfo.isFollowed
        ) {
            changeFollowStatus(it)
        }
        FollowCountStatus(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            profileInfo.followerCount,
            profileInfo.followingCount
        )
    }
}

@Composable
private fun FollowStatus(
    modifier: Modifier,
    isAlreadyFollowed: Boolean,
    changeFollowStatus: (Boolean) -> Unit
) {
    val followText =
        if (isAlreadyFollowed) stringResource(id = R.string.otherFollowed) else stringResource(
            id = R.string.otherFollow
        )
    val bgColor = if (isAlreadyFollowed) Gray2 else Main1
    val textColor = if (isAlreadyFollowed) Gray7 else Main4
    val borderColor = if (isAlreadyFollowed) Gray5 else Main4
    MyText(
        text = followText,
        color = textColor,
        fontWeight = FontWeight.Bold,
        fontSize = dpToSp(dp = 14.dp),
        modifier = modifier
            .padding(top = 12.dp)
            .clip(RoundedCornerShape(22.dp))
            .clickable {
                changeFollowStatus(!isAlreadyFollowed)
            }
            .background(
                color = bgColor,
                shape = RoundedCornerShape(22.dp)
            )
            .border(
                color = borderColor,
                width = 1.dp,
                shape = RoundedCornerShape(22.dp)
            )
            .padding(horizontal = 22.dp, vertical = 3.dp)
    )
}

@Composable
private fun FollowCountStatus(
    modifier: Modifier,
    followerCount: String,
    followingCount: String
) {
    Row(
        modifier = modifier
            .padding(top = 14.dp, bottom = 28.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        FollowCountTextView(
            modifier = Modifier.align(Alignment.CenterVertically),
            stringResource(id = R.string.followerText),
            followerCount
        )
        Spacer(modifier = Modifier.width(16.dp))
        FollowCountTextView(
            modifier = Modifier.align(Alignment.CenterVertically),
            stringResource(id = R.string.followingText), followingCount
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
            color = Gray9,
            fontSize = dpToSp(dp = 13.dp),
            modifier = Modifier
                .padding(end = 8.dp)
                .align(Alignment.CenterVertically)
        )
        MyText(
            text = number,
            color = Gray9,
            fontSize = dpToSp(dp = 15.dp),
            modifier = Modifier
                .widthIn(min = 28.dp)
                .align(Alignment.CenterVertically)
        )
    }
}


@Composable
@Preview
private fun OtherHomeProfilePreview() {
    OtherHomeProfile(
        profileInfo = TopProfile(
            isFollowed = false,
            name = "안녕다른사람",
            imgUrl = null,
            percent = 0.3f,
            badgeType = null,
            badgeTitle = "신나는여행자",
            bio = "나는다른사람이라고해요",
            followerCount = "10",
            followingCount = "20"
        ),
        {}, {}
    )
}