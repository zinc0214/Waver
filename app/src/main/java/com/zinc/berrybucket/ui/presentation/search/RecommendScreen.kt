package com.zinc.berrybucket.ui.presentation.search

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.berrybucket.ui.presentation.SearchEvent
import kotlin.math.min

@Composable
fun RecommendScreen(
    onSearchEvent: (SearchEvent.GoToSearch) -> Unit
) {
    val viewModel: SearchViewModel = hiltViewModel()
    viewModel.loadSearchRecommendCategoryItems()
    viewModel.loadRecommendList()

    val searchRecommendCategoryItems by viewModel.recommendCategoryItems.observeAsState()
    val recommendList by viewModel.recommendList.observeAsState()

    val scrollState = rememberLazyListState()
    val isScrolled = scrollState.firstVisibleItemIndex != 0
    val scrollOffset: Float = min(1f, 1 - (scrollState.firstVisibleItemScrollOffset / 50f))

    val maxAppBarHeight = 274.dp
    val minAppBarHeight = 100.dp
    var height by remember {
        mutableStateOf(0f)
    }
    val density = LocalDensity.current
    val animatedHeight by animateDpAsState(targetValue = with(density) { height.toDp() })
    Column(modifier = Modifier.fillMaxSize()) {
        RecommendTopBar(
            modifier = Modifier
                .fillMaxWidth(),
            height = animatedHeight,
            recommendCategoryItemList = searchRecommendCategoryItems,
            editViewClicked = {
                onSearchEvent.invoke(SearchEvent.GoToSearch)
            }
        )

        RecommendDivider()

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
            )
        }
    }
}