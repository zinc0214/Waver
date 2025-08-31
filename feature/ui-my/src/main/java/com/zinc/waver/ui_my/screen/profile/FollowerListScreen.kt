package com.zinc.waver.ui_my.screen.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.common.models.OtherProfileInfo
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.presentation.component.RoundChip
import com.zinc.waver.ui.presentation.component.TitleIconType
import com.zinc.waver.ui.presentation.component.TitleView
import com.zinc.waver.ui.presentation.screen.blank.FriendsBlank
import com.zinc.waver.ui_my.R
import com.zinc.waver.ui_my.viewModel.FollowViewModel

@Composable
fun FollowerListScreen(
    goToBack: () -> Unit,
    goToSetting: () -> Unit,
    goToOtherHome: (String) -> Unit
) {

    val viewModel: FollowViewModel = hiltViewModel()
    val followerList by viewModel.followerList.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFollowList()
    }

    FollowerListScreen(
        goToBack = goToBack, goToSetting = goToSetting, goToOtherHome = goToOtherHome,
        clickToFollow = { viewModel.requestFollow(it) },
        followerList = followerList.orEmpty()
    )
}

@Composable
private fun FollowerListScreen(
    goToBack: () -> Unit,
    goToSetting: () -> Unit,
    goToOtherHome: (String) -> Unit,
    clickToFollow: (OtherProfileInfo) -> Unit,
    followerList: List<OtherProfileInfo>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        TitleView(
            title = stringResource(id = R.string.followerText) + " ${followerList.size}",
            leftIconType = TitleIconType.BACK,
            rightText = if (followerList.isNotEmpty()) stringResource(id = R.string.followSetting) else "",
            isDividerVisible = true,
            onLeftIconClicked = {
                goToBack()
            },
            onRightTextClicked = {
                goToSetting()
            }
        )

        if (followerList.isEmpty()) {
            FriendsBlank(
                guideText = stringResource(R.string.followerBlankText),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
        }

        if (followerList.isNotEmpty()) {
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
                    FollowerItem(
                        member = member,
                        goToOtherHome = goToOtherHome,
                        clickToFollow = clickToFollow
                    )
                })
            }
        }
    }
}


@Composable
private fun FollowerItem(
    member: OtherProfileInfo,
    goToOtherHome: (String) -> Unit,
    clickToFollow: (OtherProfileInfo) -> Unit
) {

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

        FollowItemView(
            info = member,
            modifier = Modifier
                .padding(end = if (member.mutualFollow) 16.dp else 0.dp)
                .weight(1f),
            goToOtherHome = goToOtherHome,
        )

        if (!member.mutualFollow) {
            RoundChip(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clickable {
                        clickToFollow(member)
                    },
                chipRadius = 15.dp,
                textModifier = Modifier.padding(horizontal = 22.dp, vertical = 3.dp),
                selectedTextColor = Main4,
                unSelectedTextColor = Main4,
                unSelectedBorderColor = Main4,
                text = stringResource(id = R.string.followingButton),
                isSelected = false,
                fontSize = 13.dp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FollowingListSettingScreenPreview1() {
    FollowerListScreen(
        goToBack = {},
        goToSetting = {},
        goToOtherHome = {},
        clickToFollow = {},
        followerList = listOf()
    )
}

@Preview(showBackground = true)
@Composable
private fun FollowingListSettingScreenPreview2() {
    FollowerListScreen(
        goToBack = {},
        goToSetting = {},
        goToOtherHome = {},
        clickToFollow = {},
        followerList = listOf(
            OtherProfileInfo(id = "1", imgUrl = null, name = "Ava Ayala", mutualFollow = true),
            OtherProfileInfo(id = "2", imgUrl = null, name = "Ava KKA", mutualFollow = false),
            OtherProfileInfo(
                id = "3",
                imgUrl = null,
                name = "Ava KKA kskpkdskw dkwodkdwkdkwpwkdowkp",
                mutualFollow = false
            ),
            OtherProfileInfo(
                id = "4",
                imgUrl = null,
                name = "Ava KKA kskpkdskw dkwodkdwkdkwpwkdowkp",
                mutualFollow = true
            )
        )
    )
}