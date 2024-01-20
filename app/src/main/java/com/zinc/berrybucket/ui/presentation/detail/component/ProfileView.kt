package com.zinc.berrybucket.ui.presentation.detail.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.model.WriterProfileInfoUi
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.presentation.component.ProfileView
import com.zinc.berrybucket.ui.util.dpToSp

@Composable
fun DetailProfileView(
    writerProfileInfo: WriterProfileInfoUi,
    goToOtherProfile: (String) -> Unit
) {
    ProfileView(
        modifier = Modifier
            .padding(top = 28.dp)
            .padding(horizontal = 12.dp)
            .clickable {
                goToOtherProfile(writerProfileInfo.userId)
            },
        imageSize = 48.dp,
        profileSize = 44.dp,
        profileRadius = 16.dp,
        badgeSize = Pair(24.dp, 27.dp),
        nickNameTextSize = dpToSp(14.dp),
        titlePositionTextSize = dpToSp(13.dp),
        nickNameTextColor = Gray10,
        profileInfo = writerProfileInfo.toUi()
    )
}