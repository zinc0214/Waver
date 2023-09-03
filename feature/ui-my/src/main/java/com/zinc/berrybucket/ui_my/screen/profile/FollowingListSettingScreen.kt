package com.zinc.berrybucket.ui_my.screen.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.berrybucket.ui.design.theme.Gray9
import com.zinc.berrybucket.ui.presentation.component.RoundChip
import com.zinc.berrybucket.ui.presentation.component.TitleIconType
import com.zinc.berrybucket.ui.presentation.component.TitleView
import com.zinc.berrybucket.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.berrybucket.ui_my.R
import com.zinc.berrybucket.ui_my.viewModel.FollowViewModel

@Composable
fun FollowingListSettingScreen(
    goToBack: () -> Unit
) {

    val viewModel: FollowViewModel = hiltViewModel()
    val apiFailed by viewModel.loadFail.observeAsState()
    val followingList by viewModel.followingList.observeAsState()
    val apiFailDialogShow = remember { mutableStateOf(false) }
    val apiFailState = remember { mutableStateOf(apiFailed) }
    val followingListState = remember { mutableStateOf(followingList) }

    if (followingList.isNullOrEmpty()) {
        viewModel.loadFollowingList()
    }

    LaunchedEffect(followingList) {
        followingListState.value = followingList
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

    Column(modifier = Modifier.fillMaxSize()) {
        followingListState.value.let { followingList ->
            TitleView(
                title = stringResource(id = R.string.unfollowingText),
                leftIconType = TitleIconType.BACK,
                isDividerVisible = true,
                onLeftIconClicked = {
                    goToBack()
                }
            )

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
                items(items = followingListState.value.orEmpty(), key = { member ->
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
                                }
                        )

                        RoundChip(
                            modifier = Modifier
                                .defaultMinSize(minWidth = 56.dp, minHeight = 30.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .clickable {
                                    viewModel.requestUnfollow(member)
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
}

@Preview
@Composable
private fun FollowingListSettingScreenPreview() {
    val viewModel: FollowViewModel = hiltViewModel()

    FollowingListSettingScreen(
        goToBack = {}

    )
}