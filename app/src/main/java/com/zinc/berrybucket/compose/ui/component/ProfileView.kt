package com.zinc.berrybucket.compose.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.zinc.berrybucket.model.ProfileInfo

@Composable
fun ProfileView(
    modifier: Modifier = Modifier,
    imageSize: Dp = 36.dp,
    profileSize: Dp = 32.dp,
    profileRadius: Dp = 12.dp,
    badgeSize: Pair<Dp, Dp> = Pair(18.dp, 20.dp),
    nickNameTextSize: TextUnit = 12.sp,
    titlePositionTextSize: TextUnit = 12.sp,
    nickNameTextColor: Color = Gray9,
    titlePositionTextColor: Color = Gray7,
    profileInfo: ProfileInfo
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        ProfileImageView(
            modifier = Modifier.align(Alignment.CenterVertically),
            imageSize = imageSize,
            profileSize = profileSize,
            badgeSize = badgeSize,
            profileRadius = profileRadius,
            profile = profileInfo.profileImage,
            badge = profileInfo.badgeImage
        )
        ProfileTextView(
            nickNameTextSize = nickNameTextSize,
            titlePositionTextSize = titlePositionTextSize,
            nickNameTextColor = nickNameTextColor,
            titlePositionTextColor = titlePositionTextColor,
            titlePosition = profileInfo.titlePosition,
            nickname = profileInfo.nickName,
            modifier = Modifier.align(Alignment.CenterVertically)
        )

    }
}


@Composable
private fun ProfileImageView(
    modifier: Modifier = Modifier,
    imageSize: Dp,
    profileSize: Dp,
    profileRadius: Dp,
    badgeSize: Pair<Dp, Dp>,
    profile: String,
    badge: String
) {
    Box(
        modifier = modifier.size(imageSize),
        content = {
            Image(
                painter = painterResource(id = R.drawable.kakao),
                contentDescription = stringResource(
                    id = R.string.feedProfileImage
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(profileSize, profileSize)
                    .aspectRatio(1f)
                    .clip(shape = RoundedCornerShape(profileRadius))
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
private fun ProfileTextView(
    nickNameTextSize: TextUnit,
    titlePositionTextSize: TextUnit,
    nickNameTextColor: Color,
    titlePositionTextColor: Color,
    titlePosition: String,
    nickname: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(start = 7.dp)) {
        Text(
            text = titlePosition,
            fontSize = titlePositionTextSize,
            color = titlePositionTextColor
        )
        Text(
            text = nickname,
            fontSize = nickNameTextSize,
            color = nickNameTextColor
        )
    }
}