package com.zinc.waver.ui_my.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.zinc.common.models.OtherProfileInfo
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.presentation.component.TitleIconType
import com.zinc.waver.ui.presentation.component.TitleView
import com.zinc.waver.ui.presentation.screen.blank.FriendsBlank
import com.zinc.waver.ui_my.R
import com.zinc.waver.ui_my.viewModel.FollowViewModel

@Composable
fun FollowerListSettingScreen(
    goToBack: () -> Unit,
    goToOtherHome: (String) -> Unit
) {
    val viewModel: FollowViewModel = hiltViewModel()
    val followerList by viewModel.followerList.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFollowList()
    }

    FollowerListSettingScreen(
        followerList = followerList.orEmpty(),
        goToBack = goToBack,
        goToOtherHome = goToOtherHome,
        modifier = Modifier.background(Gray1)
    )
}

@Composable
private fun FollowerListSettingScreen(
    followerList: List<OtherProfileInfo>,
    goToBack: () -> Unit,
    goToOtherHome: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        TitleView(
            title = stringResource(id = R.string.followerSetting),
            leftIconType = TitleIconType.BACK,
            isDividerVisible = true,
            onLeftIconClicked = {
                goToBack()
            }
        )

        if (followerList.isEmpty()) {
            FriendsBlank(guideText = stringResource(R.string.followerBlankText))
        }

        LazyColumn(
            contentPadding = PaddingValues(
                top = 24.dp,
                start = 24.dp,
                end = 16.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(items = followerList, key = { member ->
                member.id
            }, itemContent = { member ->

                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                    val (followingMember, unFollowButton) = createRefs()

                    FollowItemView(
                        info = member,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .constrainAs(followingMember) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                                end.linkTo(unFollowButton.start)
                                width = Dimension.fillToConstraints
                            },
                        goToOtherHome = goToOtherHome
                    )
                }
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FollowerListSettingScreenPreview1() {
    FollowerListSettingScreen(
        followerList = listOf(),
        goToBack = {},
        goToOtherHome = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun FollowerListSettingScreenPreview2() {
    FollowerListSettingScreen(
        followerList = listOf(
            OtherProfileInfo(
                id = "1",
                imgUrl = null,
                name = "Ramon Dillon",
                mutualFollow = false
            )
        ),
        goToBack = {},
        goToOtherHome = {}
    )
}