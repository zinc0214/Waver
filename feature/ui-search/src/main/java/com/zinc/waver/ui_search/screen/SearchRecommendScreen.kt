package com.zinc.waver.ui_search.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.waver.ui_search.component.RecommendListView
import com.zinc.waver.ui_search.component.RecommendTopBar
import com.zinc.waver.ui_search.model.SearchGoToEvent
import com.zinc.waver.ui_search.viewmodel.SearchViewModel

@Composable
fun SearchRecommendScreen(
    onSearchEvent: (SearchGoToEvent) -> Unit
) {
    val viewModel: SearchViewModel = hiltViewModel()
    viewModel.loadRecommendList()
    val recommendList by viewModel.recommendList.observeAsState()

    var isFirstItemShown by remember { mutableStateOf(true) }

    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()) {
        RecommendTopBar(
            modifier = Modifier
                .fillMaxWidth(),
            isFirstItemShown = isFirstItemShown,
            editViewClicked = {
                onSearchEvent.invoke(SearchGoToEvent.GoToSearch)
            }
        )

        recommendList?.let { list ->
            RecommendListView(
                recommendList = list,
                bucketClicked = { bucketId, userId ->
                    onSearchEvent.invoke(SearchGoToEvent.GoToOpenBucket(bucketId, userId))
                },
                isFirstItemShown = { isTop ->
                    isFirstItemShown = isTop
                }
            )
        }
    }
}