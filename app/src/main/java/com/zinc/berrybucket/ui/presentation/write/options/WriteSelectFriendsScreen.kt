package com.zinc.berrybucket.ui.presentation.write.options

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.flowlayout.FlowRow
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.WriteFriend
import com.zinc.berrybucket.ui.presentation.search.SearchEditView
import com.zinc.berrybucket.ui.presentation.write.*

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

    // 검색한 친구 중 선택된 목록
    // val addedResultFriends = remember { mutableListOf<WriteFriend>() }

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
            rightText = R.string.addDesc,
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