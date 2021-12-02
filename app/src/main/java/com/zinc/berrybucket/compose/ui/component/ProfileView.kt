package com.zinc.berrybucket.compose.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.Gray7
import com.zinc.berrybucket.compose.theme.Gray9
import com.zinc.berrybucket.model.FeedInfo

@Composable
fun ProfileView(
    modifier: Modifier = Modifier,
    profileSize: Dp = 36.dp,
    badgeSize: Pair<Dp, Dp> = Pair(18.dp, 20.dp),
    textSize: TextUnit = 12.sp,
    feedInfo: FeedInfo
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        ProfileImageView(
            modifier = Modifier.align(Alignment.CenterVertically),
            profileSize = profileSize,
            badgeSize = badgeSize,
            profile = feedInfo.profileImage,
            badge = feedInfo.badgeImage
        )
        ProfileTextView(
            textSize = textSize,
            titlePosition = feedInfo.titlePosition,
            nickname = feedInfo.nickName
        )

    }
}


@Composable
private fun ProfileImageView(
    modifier: Modifier = Modifier,
    profileSize: Dp,
    badgeSize: Pair<Dp, Dp>,
    profile: String,
    badge: String
) {
    Box(
        modifier = modifier.size(profileSize),
        content = {
            Image(
                painter = painterResource(id = R.drawable.kakao),
                contentDescription = stringResource(
                    id = R.string.feedProfileImage
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(32.dp, 32.dp)
                    .aspectRatio(1f)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .align(Alignment.TopStart)
            )
            Image(
                painter = painterResource(id = R.drawable.badge_small),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(badgeSize.first, badgeSize.second)
                    .align(Alignment.BottomEnd),
                alignment = Alignment.BottomEnd
            )
        })
}

@Composable
private fun ProfileTextView(textSize: TextUnit, titlePosition: String, nickname: String) {
    Column(modifier = Modifier.padding(start = 7.dp)) {
        Text(
            text = titlePosition,
            fontSize = textSize,
            color = Gray7
        )
        Text(
            text = nickname,
            fontSize = textSize,
            color = Gray9
        )
    }
}