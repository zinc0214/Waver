package com.zinc.berrybucket.ui_more

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.berrybucket.ui.presentation.component.PopUpView
import com.zinc.berrybucket.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.berrybucket.ui_more.components.BerryClubLabelView
import com.zinc.berrybucket.ui_more.components.MoreItemsView
import com.zinc.berrybucket.ui_more.components.MoreTitleView
import com.zinc.berrybucket.ui_more.components.MoreTopProfileView
import com.zinc.berrybucket.ui_more.models.MoreItemType
import com.zinc.berrybucket.ui_more.viewModel.MoreViewModel

@Composable
fun MoreScreen(
    modifier: Modifier = Modifier,
    moreItemClicked: (MoreItemType) -> Unit,
    goToBack: () -> Unit
) {

    var logoutPopupShow by remember { mutableStateOf(false) }
    val viewModel: MoreViewModel = hiltViewModel()

    val profileInfo by viewModel.profileInfo.observeAsState()
    val loadProfileFail by viewModel.profileLoadFail.observeAsState()

    val showApiFailDialog = remember { mutableStateOf(false) }

    if (profileInfo == null) {
        viewModel.loadMyProfile()
    }

    LaunchedEffect(key1 = loadProfileFail) {
        showApiFailDialog.value = true
    }

    Column {
        MoreTitleView()

        profileInfo?.let { info ->
            MoreTopProfileView(info, goToProfileUpdate = { moreItemClicked(MoreItemType.PROFILE) })
        }

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

    if (showApiFailDialog.value) {
        ApiFailDialog(
            title = stringResource(id = R.string.loadFailProfile),
            message = stringResource(id = R.string.retryDesc),
            dismissEvent = {
                showApiFailDialog.value = false
                goToBack()
            })
    }
}