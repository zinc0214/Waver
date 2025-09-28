package com.zinc.waver.ui_other.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.zinc.waver.ui.design.theme.Gray2
import com.zinc.waver.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.waver.ui_common.R
import com.zinc.waver.ui_other.component.OtherBucketListView
import com.zinc.waver.ui_other.component.OtherHomeProfile
import com.zinc.waver.ui_other.model.OtherHomeEvent
import com.zinc.waver.ui_other.viewmodel.OtherViewModel

@Composable
fun OtherHomeScreen(
    userId: String,
    otherHomeEvent: (OtherHomeEvent) -> Unit
) {

    val viewModel: OtherViewModel = hiltViewModel()
    val profileHomeDataAsState by viewModel.otherHomeData.observeAsState()
    val apiFailAsState by viewModel.loadFail.observeAsState()

    val profileHomeData = remember { mutableStateOf(profileHomeDataAsState) }
    val showFailDialog = remember { mutableStateOf(false) }

    val parentScrollState = rememberScrollState() // 헤더 스크롤 상태

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                return if (parentScrollState.value < parentScrollState.maxValue) {
                    // 부모가 스크롤되는 동안은 자식 스크롤 금지
                    val consumed = available.y
                    parentScrollState.dispatchRawDelta(-consumed)
                    Offset(0f, consumed)
                } else {
                    // 부모 스크롤이 끝난 후 자식 스크롤 시작
                    Offset.Zero
                }
            }
        }
    }


    LaunchedEffect(key1 = Unit) {
        viewModel.loadOtherInfo(userId)
    }
    LaunchedEffect(key1 = profileHomeDataAsState) {
        profileHomeDataAsState?.let {
            profileHomeData.value = it
        }
    }

    LaunchedEffect(key1 = apiFailAsState) {
        showFailDialog.value = apiFailAsState == true
        Log.e("ayhan", "${showFailDialog.value}, $apiFailAsState")
    }


    profileHomeData.value?.let { homeData ->
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .nestedScroll(nestedScrollConnection)
                .verticalScroll(parentScrollState)
        ) {
            OtherHomeProfile(
                profileInfo = homeData.profile,
                changeFollowStatus = { changeFollow ->
                    viewModel.changeFollowStatus(userId, changeFollow)
                },
                goToBack = {
                    otherHomeEvent.invoke(OtherHomeEvent.GoToBack)
                }
            )

            OtherBucketListView(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Gray2)
                    .fillMaxHeight()
                    .padding(top = 24.dp)
                    .padding(horizontal = 16.dp),
                bucketList = homeData.bucketList,
                itemClicked = { bucketId ->
                    otherHomeEvent.invoke(OtherHomeEvent.GoToOtherBucket(bucketId, userId))
                }
            )
        }
    }

    if (showFailDialog.value) {
        ApiFailDialog(
            title = stringResource(id = R.string.apiFailTitle),
            message = stringResource(id = R.string.apiFailMessage)
        ) {
            showFailDialog.value = false
            otherHomeEvent(OtherHomeEvent.GoToBack)
        }
    }
}