package com.zinc.waver.ui_my.screen.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.common.models.OtherProfileInfo
import com.zinc.waver.ui.presentation.component.TitleIconType
import com.zinc.waver.ui.presentation.component.TitleView
import com.zinc.waver.ui_my.R
import com.zinc.waver.ui_my.viewModel.FollowViewModel

@Composable
fun FollowingListScreen(
    goToBack: () -> Unit,
    goToSetting: () -> Unit,
    goToOtherHome: (String) -> Unit
) {

    val viewModel: FollowViewModel = hiltViewModel()
    val followingList by viewModel.followingList.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFollowingList()
    }

    FollowingListScreen(
        followingList = followingList.orEmpty(),
        goToBack = goToBack,
        goToSetting = goToSetting,
        goToOtherHome = goToOtherHome,
    )
}

@Composable
private fun FollowingListScreen(
    followingList: List<OtherProfileInfo>,
    goToBack: () -> Unit,
    goToSetting: () -> Unit,
    goToOtherHome: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        TitleView(
            title = stringResource(id = R.string.followingText) + " ${followingList.size}",
            leftIconType = TitleIconType.BACK,
            rightText = if (followingList.isNotEmpty()) stringResource(id = R.string.followSetting) else "",
            isDividerVisible = true,
            onLeftIconClicked = {
                goToBack()
            },
            onRightTextClicked = {
                goToSetting()
            }
        )

        if (followingList.isEmpty()) {
            FriendsBlank(
                guideText = stringResource(R.string.followingBlankText),
                modifier = Modifier.fillMaxSize()
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(items = followingList, key = { member ->
                member.id
            }, itemContent = { member ->
                FollowItemView(modifier = Modifier.fillMaxWidth(), info = member, goToOtherHome)
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FollowingListScreenPreview1() {
    FollowingListScreen(
        followingList = listOf(),
        goToBack = {},
        goToSetting = {},
        goToOtherHome = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun FollowingListScreenPreview2() {
    FollowingListScreen(
        followingList = listOf(
            OtherProfileInfo(
                id = "definiebas",
                imgUrl = null,
                name = "Ronda Murray",
                mutualFollow = false
            )
        ),
        goToBack = {},
        goToSetting = {},
        goToOtherHome = {}
    )
}