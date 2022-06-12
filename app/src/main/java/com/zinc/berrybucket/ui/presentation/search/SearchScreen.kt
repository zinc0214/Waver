package com.zinc.berrybucket.ui.presentation.search

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlin.math.min

@Composable
fun SearchScreen() {
    val coroutineScope = rememberCoroutineScope()

    val viewModel: SearchViewModel = hiltViewModel()
    viewModel.loadSearchRecommendCategoryItems()
    viewModel.loadRecommendList()

    val searchRecommendCategoryItems by viewModel.recommendCategoryItems.observeAsState()
    val recommendList by viewModel.recommendList.observeAsState()

//    AndroidView(modifier = Modifier.fillMaxSize(), factory = { context ->
//        SearchView(context).apply {
//            setComposeView {
//                // go to search view
//            }
//            searchRecommendCategoryItems?.let {
//                setRecommendView(it)
//            }
//            recommendList?.let {
//                setRecommendBucketList(it)
//            }
//        }
//    })

    val scrollState = rememberLazyListState()
    val isScrolled = scrollState.firstVisibleItemIndex != 0
    val scrollOffset: Float = min(1f, 1 - (scrollState.firstVisibleItemScrollOffset / 50f))
    Log.e("ayhan", "isScrolled ; $scrollOffset")


    val maxAppBarHeight = 274.dp
    val minAppBarHeight = 100.dp
    var height by remember {
        mutableStateOf(0f)
    }
    val density = LocalDensity.current
    val animatedHeight by animateDpAsState(targetValue = with(density) { height.toDp() })
    Column(modifier = Modifier.fillMaxSize()) {
        SearchTopBar(
            modifier = Modifier
                .fillMaxWidth(),
            height = animatedHeight,
            viewModel = viewModel,
            scrollOffset = scrollOffset,
            isScrolled = isScrolled,
            editViewClicked = {

            }
        )

        SearchDivider()

        recommendList?.let { list ->
            RecommendListView(
                onOffsetChanged = {
                    Log.d("OFFSET", "$it")
                    height = it
                },
                maxAppBarHeight = maxAppBarHeight,
                minAppBarHeight = minAppBarHeight,
                recommendList = list,
                listState = scrollState
            )
        }

    }

//    Column(modifier = Modifier.fillMaxSize()) {
//        SearchTopBar(
//            modifier = Modifier.fillMaxWidth(),
//            viewModel = viewModel,
//            scrollOffset = scrollOffset,
//            isScrolled = isScrolled,
//            editViewClicked = {
//
//            }
//        )
//
//        recommendList?.let {
//            RecommendListView(it, scrollState)
//        }
//    }
}

@Composable
fun DismissibleAppBar() {

}
