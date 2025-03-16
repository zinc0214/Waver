package com.zinc.waver.ui_my.screen.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.common.models.OtherProfileInfo
import com.zinc.waver.ui.design.theme.Gray9
import com.zinc.waver.ui.presentation.component.RoundChip
import com.zinc.waver.ui.presentation.component.TitleIconType
import com.zinc.waver.ui.presentation.component.TitleView
import com.zinc.waver.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.waver.ui.presentation.screen.blank.FriendsBlank
import com.zinc.waver.ui_my.R
import com.zinc.waver.ui_my.viewModel.FollowViewModel

@Composable
fun FollowingListSettingScreen(
    goToBack: () -> Unit,
    goToOtherHome: (String) -> Unit
) {

    val viewModel: FollowViewModel = hiltViewModel()
    val apiFailed by viewModel.loadFail.observeAsState()
    val followingListState by viewModel.followingList.observeAsState()
    val apiFailDialogShow = remember { mutableStateOf(false) }
    val apiFailState = remember { mutableStateOf(apiFailed) }
    var followingList by remember { mutableStateOf(followingListState) }

    LaunchedEffect(Unit) {
        viewModel.loadFollowingList()
    }

    LaunchedEffect(followingListState) {
        followingList = followingListState
    }

    LaunchedEffect(apiFailed) {
        if (apiFailed != null) {
            apiFailDialogShow.value = true
            apiFailState.value = apiFailed
        }
    }


    if (apiFailDialogShow.value) {
        apiFailState.value?.let { failData ->
            ApiFailDialog(failData.first.orEmpty(), failData.second.orEmpty()) {
                apiFailDialogShow.value = false
            }
        }
    }

    FollowingListSettingScreen(
        followingList = followingList.orEmpty(),
        requestUnfollow = {
            viewModel.requestUnfollow(it)
        },
        goToBack = goToBack,
        goToOtherHome = goToOtherHome,
    )
}

@Composable
private fun FollowingListSettingScreen(
    followingList: List<OtherProfileInfo>,
    requestUnfollow: (OtherProfileInfo) -> Unit,
    goToBack: () -> Unit,
    goToOtherHome: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        TitleView(
            title = stringResource(id = R.string.unfollowingText),
            leftIconType = TitleIconType.BACK,
            isDividerVisible = true,
            onLeftIconClicked = {
                goToBack()
            }
        )

        if (followingList.isEmpty()) {
            FriendsBlank(guideText = stringResource(R.string.followingBlankText))
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
            items(items = followingList, key = { member ->
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

                    RoundChip(
                        modifier = Modifier
                            .defaultMinSize(minWidth = 56.dp, minHeight = 30.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .clickable {
                                requestUnfollow(member)
                            }
                            .constrainAs(unFollowButton) {
                                top.linkTo(parent.top)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)

                            },
                        chipRadius = 15.dp,
                        textModifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp),
                        selectedTextColor = Gray9,
                        unSelectedTextColor = Gray9,
                        text = stringResource(id = R.string.unfollowingButton),
                        isSelected = false,
                        fontWeight = FontWeight.Normal
                    )
                }
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FollowingListSettingScreenPreview() {
    FollowingListSettingScreen(
        followingList = listOf(
            OtherProfileInfo(
                id = "1",
                imgUrl = null,
                name = "Brandy Rich",
                mutualFollow = false
            ),
            OtherProfileInfo(
                id = "2",
                imgUrl = null,
                name = "Brandy Rich",
                mutualFollow = true
            )
        ),
        requestUnfollow = {},
        goToBack = {},
        goToOtherHome = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun FollowingListSettingScreenPreview2() {
    FollowingListSettingScreen(
        followingList = listOf(),
        requestUnfollow = {},
        goToBack = {},
        goToOtherHome = {}
    )
}