package com.zinc.berrybucket.ui_search.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui_search.component.RecommendListView
import com.zinc.berrybucket.ui_search.component.RecommendTopBar
import com.zinc.berrybucket.ui_search.model.SearchGoToEvent
import com.zinc.berrybucket.ui_search.viewmodel.SearchViewModel

@Composable
fun SearchRecommendScreen(
    onSearchEvent: (SearchGoToEvent) -> Unit
) {
    val viewModel: SearchViewModel = hiltViewModel()
    viewModel.loadRecommendList()
    val recommendList by viewModel.recommendList.observeAsState()

    var isFirstItemShown by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize()) {
        rememberSystemUiController().setSystemBarsColor(Gray1)
        RecommendTopBar(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
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