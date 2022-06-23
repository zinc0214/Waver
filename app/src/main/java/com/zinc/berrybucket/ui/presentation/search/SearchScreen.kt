package com.zinc.berrybucket.ui.presentation.search

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SearchScreen(
    goTGoEvent: () -> Unit
) {
    val viewModel: SearchViewModel = hiltViewModel()
    val searchRecommendItems by viewModel.searchRecommendItems.observeAsState()
    val searchResultItems by viewModel.searchResultItems.observeAsState()
    viewModel.loadSearchRecommendItems()

    val listScrollState = rememberLazyListState()
    var searchWord by remember { mutableStateOf("") }


    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (topAppBar, searchResultView) = createRefs()

        SearchTopAppBar(
            listState = listScrollState,
            title = searchWord,
            closeClicked = {
                goTGoEvent.invoke()
            },
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(topAppBar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
        )

        Column(modifier = Modifier
            .constrainAs(searchResultView) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(topAppBar.bottom)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            }
            .scrollable(state = listScrollState, orientation = Orientation.Vertical)) {
            SearchEditView(
                onImeAction = {
                    viewModel.loadSearchResult(searchWord)
                },
                searchTextChange = {
                    searchWord = it
                }
            )
            if (searchWord.isEmpty() && searchResultItems == null) {
                searchRecommendItems?.let {
                    RecommendKeyWordView(it)
                }
            }
            searchResultItems?.let {
                SearchResultView(it)
            }
        }
    }
}
