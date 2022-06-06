package com.zinc.berrybucket.compose.ui.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.berrybucket.customUi.SearchView
import com.zinc.berrybucket.presentation.search.SearchViewModel

@Composable
fun SearchScreen() {
    val coroutineScope = rememberCoroutineScope()

    val viewModel: SearchViewModel = hiltViewModel()
    viewModel.loadSearchRecommendCategoryItems()
    viewModel.loadRecommendList()

    val searchRecommendCategoryItems by viewModel.searchRecommendCategoryItems.observeAsState()
    val recommendList by viewModel.recommendList.observeAsState()

    AndroidView(modifier = Modifier.fillMaxSize(), factory = { context ->
        SearchView(context).apply {
            setComposeView {
                // go to search view
            }
            searchRecommendCategoryItems?.let {
                setRecommendView(it)
            }
            recommendList?.let {
                setRecommendBucketList(it)
            }
        }
    })
}
