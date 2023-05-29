package com.zinc.berrybucket.ui_more

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.zinc.berrybucket.ui.presentation.component.PopUpView
import com.zinc.berrybucket.ui_more.components.BerryClubLabelView
import com.zinc.berrybucket.ui_more.components.MoreItemsView
import com.zinc.berrybucket.ui_more.components.MoreTitleView
import com.zinc.berrybucket.ui_more.components.MoreTopProfileView
import com.zinc.berrybucket.ui_more.models.MoreItemType
import com.zinc.berrybucket.ui_more.models.UIMoreMyProfileInfo
import com.zinc.common.models.BadgeType

@Composable
fun MoreScreen(modifier: Modifier = Modifier, moreItemClicked: (MoreItemType) -> Unit) {

    var logoutPopupShow by remember { mutableStateOf(false) }

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
            if (it == MoreItemType.LOGOUT) {
                logoutPopupShow = true
            } else {
                moreItemClicked(it)
            }
        }
    }

    if (logoutPopupShow) {
        PopUpView(
            title = "로그아웃 하시겠어요?",
            cancelText = "취소",
            positiveText = "로그아웃",
            cancelClicked = {
                logoutPopupShow = false
            },
            positiveClicked = {
                logoutPopupShow = false
            },
            onDismissRequest = { logoutPopupShow = false },
            clickable = true
        )
    }

}