package com.zinc.berrybucket.ui.presentation.search

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.zinc.berrybucket.util.nav.SearchEvent

@Composable
fun RecommendScreen(
    onSearchEvent: (SearchEvent.GoToSearch) -> Unit
) {
    val viewModel: SearchViewModel = hiltViewModel()
    viewModel.loadRecommendList()
    val recommendList by viewModel.recommendList.observeAsState()

    val maxAppBarHeight = 156.dp
    val minAppBarHeight = 106.dp
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
            editViewClicked = {
                onSearchEvent.invoke(SearchEvent.GoToSearch)
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
            )
        }
    }
}