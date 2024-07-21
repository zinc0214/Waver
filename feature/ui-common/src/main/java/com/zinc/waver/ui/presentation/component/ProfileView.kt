package com.zinc.waver.ui.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.zinc.waver.model.UiProfileInfo
import com.zinc.waver.ui.design.theme.Gray7
import com.zinc.waver.ui.design.theme.Gray9
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_common.R

@Composable
fun ProfileView(
    modifier: Modifier = Modifier,
    imageSize: Dp = 36.dp,
    profileSize: Dp = 32.dp,
    profileRadius: Dp = 12.dp,
    badgeSize: Pair<Dp, Dp> = Pair(18.dp, 20.dp),
    nickNameTextSize: TextUnit = dpToSp(12.dp),
    titlePositionTextSize: TextUnit = dpToSp(12.dp),
    nickNameTextColor: Color = Gray9,
    titlePositionTextColor: Color = Gray7,
    profileInfo: UiProfileInfo
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
            profileUrl = profileInfo.profileImage,
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
    profileUrl: String?,
    badge: String
) {
    Box(
        modifier = modifier.size(imageSize),
        content = {
            Image(
                painter = painterResource(id = R.drawable.btn_32_like_on),
                contentDescription = stringResource(
                    id = R.string.profileImgDesc
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
        MyText(
            text = titlePosition,
            fontSize = titlePositionTextSize,
            color = titlePositionTextColor
        )
        MyText(
            text = nickname,
            fontSize = nickNameTextSize,
            color = nickNameTextColor
        )
    }
}

@Composable
@Preview
private fun ProfilePreview() {
    ProfileView(
        profileInfo = UiProfileInfo(
            profileImage = "",
            badgeImage = "",
            titlePosition = "나는 아이스크름이 되는거야",
            nickName = "멋쟁이 토마토"
        )
    )
}