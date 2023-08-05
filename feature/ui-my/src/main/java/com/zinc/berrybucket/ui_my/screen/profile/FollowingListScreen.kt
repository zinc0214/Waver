package com.zinc.berrybucket.ui_my.screen.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.berrybucket.ui.presentation.component.TitleIconType
import com.zinc.berrybucket.ui.presentation.component.TitleView
import com.zinc.berrybucket.ui_my.R
import com.zinc.berrybucket.ui_my.viewModel.FollowViewModel

@Composable
fun FollowingListScreen(
    goToBack: () -> Unit,
    goToSetting: () -> Unit
) {

    val viewModel: FollowViewModel = hiltViewModel()
    viewModel.loadFollowList()
    val followingList by viewModel.followingList.observeAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        followingList?.let { followingList ->
            TitleView(
                title = stringResource(id = R.string.followingText) + " ${followingList.size}",
                leftIconType = TitleIconType.BACK,
                rightText = stringResource(id = R.string.followSetting),
                isDividerVisible = true,
                onLeftIconClicked = {
                    goToBack()
                },
                onRightTextClicked = {
                    goToSetting()
                }
            )

            LazyColumn(
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(items = followingList, key = { member ->
                    member.id
                }, itemContent = { member ->
                    FollowItemView(modifier = Modifier.fillMaxWidth(), info = member)
                })
            }
        }
    }
}