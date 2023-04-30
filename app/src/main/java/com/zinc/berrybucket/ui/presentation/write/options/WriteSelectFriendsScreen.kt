package com.zinc.berrybucket.ui.presentation.write.options

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.flowlayout.FlowRow
import com.zinc.berrybucket.model.WriteFriend
import com.zinc.berrybucket.ui.presentation.search.SearchEditView
import com.zinc.berrybucket.ui.presentation.write.AddedFriendItem
import com.zinc.berrybucket.ui.presentation.write.ShowAllFriendItem
import com.zinc.berrybucket.ui.presentation.write.WriteAppBar
import com.zinc.berrybucket.ui.presentation.write.WriteAppBarClickEvent
import com.zinc.berrybucket.ui.presentation.write.WriteSelectFriendItem
import com.zinc.berrybucket.ui.presentation.write.WriteViewModel

@Composable
fun WriteSelectFriendsScreen(
    closeClicked: () -> Unit,
    selectedFriends: List<WriteFriend>,
    addFriendsClicked: (List<WriteFriend>) -> Unit
) {
    val viewModel: WriteViewModel = hiltViewModel()
    val searchFriendsResult by viewModel.searchFriendsResult.observeAsState()

    // 이미 선택된 친구가 5명이 넘는 경우
    val needShowAllFriendButton = remember { mutableStateOf(selectedFriends.size > 5) }

    // 최종 친구 목록
    var updateFriends by remember { mutableStateOf(selectedFriends) }

    val scrollState = rememberLazyListState()
    val searchWord = remember { mutableStateOf("") }

    viewModel.clearFriendsData()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        WriteAppBar(
            modifier = Modifier.fillMaxWidth(),
            nextButtonClickable = true,
            rightText = com.zinc.berrybucket.ui_common.R.string.addDesc,
            clickEvent = {
                when (it) {
                    WriteAppBarClickEvent.CloseClicked -> {
                        closeClicked()
                    }

                    WriteAppBarClickEvent.NextClicked -> {
                        addFriendsClicked(updateFriends)
                    }
                }
            },
            isShowDivider = false
        )

        SearchEditView(
            onImeAction = {
                viewModel.searchFriends(it)
            },
            searchTextChange = {
                searchWord.value = it
            },
            currentSearchWord = searchWord
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            state = scrollState,
            contentPadding = PaddingValues(bottom = 50.dp)
        ) {
            if (updateFriends.isNotEmpty()) {
                item {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp),
                        mainAxisSpacing = 12.dp,
                        crossAxisSpacing = 8.dp,
                    ) {
                        val list =
                            if (needShowAllFriendButton.value) updateFriends.take(5) else updateFriends
                        list.forEach {
                            AddedFriendItem(
                                writeFriend = it,
                                deleteFriend = { friend ->
                                    updateFriends = updateFriends - friend
                                })
                        }
                        if (needShowAllFriendButton.value) {
                            ShowAllFriendItem(clicked = {
                                needShowAllFriendButton.value = false
                            })
                        }
                    }
                }
            }

            searchFriendsResult?.let { friends ->
                item {
                    Spacer(modifier = Modifier.height(15.dp))
                }

                items(
                    items = friends,
                    itemContent = { friend ->
                        var selected = updateFriends.any { it == friend }
                        WriteSelectFriendItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                                .clickable(enabled = !selected, onClick = {
                                    updateFriends = updateFriends + friend
                                    selected = !selected
                                }),
                            writeFriend = friend,
                            isSelected = selected
                        )
                    })
            }
        }
    }
}