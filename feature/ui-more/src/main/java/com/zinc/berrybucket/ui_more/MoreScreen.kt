package com.zinc.berrybucket.ui_more

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zinc.berrybucket.ui_more.components.BerryClubLabelView
import com.zinc.berrybucket.ui_more.components.MoreItemsView
import com.zinc.berrybucket.ui_more.components.MoreTitleView
import com.zinc.berrybucket.ui_more.components.MoreTopProfileView
import com.zinc.berrybucket.ui_more.models.MoreItemType
import com.zinc.berrybucket.ui_more.models.UIMoreMyProfileInfo
import com.zinc.common.models.BadgeType

@Composable
fun MoreScreen(modifier: Modifier = Modifier, moreItemClicked: (MoreItemType) -> Unit) {
    Column {
        MoreTitleView()
        MoreTopProfileView(
            info = UIMoreMyProfileInfo(
                name = "한아라고",
                imgUrl = "",
                badgeType = BadgeType.TRIP1,
                badgeTitle = "이제 버킷리스트를 시작한",
                bio = "나는 나는 멋쟁이 토마통"
            ),
            goToProfileUpdate = { moreItemClicked(MoreItemType.PROFILE) },

            )
        BerryClubLabelView {

        }

        MoreItemsView {
            moreItemClicked(it)
        }
    }

}