package com.zinc.berrybucket.compose.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zinc.berrybucket.compose.theme.Gray10
import com.zinc.berrybucket.compose.ui.component.ProfileView
import com.zinc.berrybucket.model.ProfileInfo

@Composable
fun DetailProfileLayer(profileInfo: ProfileInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(top = 28.dp)
    ) {
        ProfileView(
            imageSize = 48.dp,
            profileSize = 44.dp,
            profileRadius = 16.dp,
            badgeSize = Pair(24.dp, 27.dp),
            nickNameTextSize = 14.sp,
            titlePositionTextSize = 13.sp,
            nickNameTextColor = Gray10,
            profileInfo = profileInfo
        )
    }
}