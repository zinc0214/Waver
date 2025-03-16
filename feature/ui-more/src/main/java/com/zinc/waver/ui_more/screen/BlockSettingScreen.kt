package com.zinc.waver.ui_more.screen

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.waver.ui.presentation.screen.blank.FriendsBlank
import com.zinc.waver.ui_more.R
import com.zinc.waver.ui_more.components.BlockMemberView
import com.zinc.waver.ui_more.components.BlockTitle
import com.zinc.waver.ui_more.viewModel.BlockSettingViewModel

@Composable
fun BlockSettingScreen(
    onBackPressed: () -> Unit,
) {
    val viewModel: BlockSettingViewModel = hiltViewModel()
    val blockedUserListAsState by viewModel.blockedUserList.observeAsState()

    val blockedUserList = remember {
        mutableStateOf(blockedUserListAsState)
    }

    LaunchedEffect(Unit) {
        viewModel.loadBlockUserList()
    }

    LaunchedEffect(blockedUserListAsState) {
        blockedUserList.value = blockedUserListAsState
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        BlockTitle {
            onBackPressed()
        }
        if (blockedUserList.value.isNullOrEmpty()) {
            FriendsBlank(
                guideText = stringResource(R.string.hasNoBlockUser),
                modifier = Modifier.fillMaxSize()
            )
        } else {
            LazyColumn(contentPadding = PaddingValues(top = 16.dp)) {
                items(items = blockedUserList.value.orEmpty(), key = { member ->
                    member.id
                }, itemContent = { member ->
                    BlockMemberView(member)
                })
            }
        }

    }
}

@Preview
@Composable
private fun BlockSettingScreenPreview() {
    BlockSettingScreen {}
}