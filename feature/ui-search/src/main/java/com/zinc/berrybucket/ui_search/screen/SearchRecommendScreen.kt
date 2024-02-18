package com.zinc.berrybucket.ui_search.screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
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

    val maxAppBarHeight = 156.dp
    val minAppBarHeight = 106.dp
    var height by remember {
        mutableFloatStateOf(0f)
    }
    val density = LocalDensity.current
    val animatedHeight by animateDpAsState(
        targetValue = with(density) { height.toDp() },
        label = "animatedHeight"
    )
    Column(modifier = Modifier.fillMaxSize()) {
        rememberSystemUiController().setSystemBarsColor(Gray1)
        RecommendTopBar(
            modifier = Modifier
                .fillMaxWidth(),
            height = animatedHeight,
            editViewClicked = {
                onSearchEvent.invoke(SearchGoToEvent.GoToSearch)
            }
        )

        recommendList?.let { list ->
            RecommendListView(
                onOffsetChanged = {
                    // 맨 마지막 아이템에서 갑자기 튀는 현상이 있음. 방지를 위함.
                    if (height == 0f || it - height <= 500) {
                        height = it
                    }
                },
                maxAppBarHeight = maxAppBarHeight,
                minAppBarHeight = minAppBarHeight,
                recommendList = list,
                bucketClicked = {
                    onSearchEvent.invoke(SearchGoToEvent.GoToOpenBucket(it))
                }
            )
        }
    }
}