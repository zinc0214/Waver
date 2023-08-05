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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.presentation.component.RoundChip
import com.zinc.berrybucket.ui.presentation.component.TitleIconType
import com.zinc.berrybucket.ui.presentation.component.TitleView
import com.zinc.berrybucket.ui_my.R
import com.zinc.berrybucket.ui_my.viewModel.FollowViewModel

@Composable
fun FollowerListScreen(
    goToBack: () -> Unit,
    goToSetting: () -> Unit
) {

    val viewModel: FollowViewModel = hiltViewModel()
    val followerList by viewModel.followerList.observeAsState()
    if (followerList.isNullOrEmpty()) {
        viewModel.loadFollowList()
    }


    Column(modifier = Modifier.fillMaxSize()) {
        followerList?.let { followerList ->
            TitleView(
                title = stringResource(id = R.string.followerText) + " ${followerList.size}",
                leftIconType = TitleIconType.BACK,
                rightText = stringResource(id = R.string.followingSetting),
                isDividerVisible = true,
                onLeftIconClicked = {
                    goToBack()
                },
                onRightTextClicked = {
                    goToSetting()
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
                                }
                        )

                        if (member.isAlreadyFollowing.isNo()) {
                            RoundChip(
                                modifier = Modifier
                                    .defaultMinSize(minWidth = 80.dp, minHeight = 30.dp)
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
                                textModifier = Modifier.padding(
                                    horizontal = 22.dp,
                                    vertical = 5.dp
                                ),
                                selectedTextColor = Main4,
                                unSelectedTextColor = Main4,
                                unSelectedBorderColor = Main4,
                                text = stringResource(id = R.string.followingButton),
                                isSelected = false,
                                fontWeight = FontWeight.Normal
                            )
                        }
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