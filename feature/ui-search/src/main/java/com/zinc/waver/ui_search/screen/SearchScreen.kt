package com.zinc.waver.ui_search.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.waver.ui.presentation.component.SearchEditView
import com.zinc.waver.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.waver.ui_search.R
import com.zinc.waver.ui_search.component.RecommendKeyWordView
import com.zinc.waver.ui_search.component.SearchResultBlankView
import com.zinc.waver.ui_search.component.SearchResultView
import com.zinc.waver.ui_search.component.SearchTopAppBar
import com.zinc.waver.ui_search.model.SearchActionEvent
import com.zinc.waver.ui_search.model.SearchGoToEvent
import com.zinc.waver.ui_search.viewmodel.SearchViewModel
import com.zinc.waver.ui_common.R as CommonR

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(
    closeEvent: () -> Unit,
    searchEvent: (SearchGoToEvent) -> Unit
) {
    val viewModel: SearchViewModel = hiltViewModel()
    val searchRecommendItemsAsState by viewModel.searchRecommendItems.observeAsState()
    val searchResultItemsAsState by viewModel.searchResultItems.observeAsState()
    val searchResultIsEmptyAsState by viewModel.searchResultIsEmpty.observeAsState()
    val loadFail by viewModel.loadFail.observeAsState()

    val listScrollState = rememberLazyListState()
    val searchResultItems = remember { mutableStateOf(searchResultItemsAsState) }
    val searchRecommendItems = remember { mutableStateOf(searchRecommendItemsAsState) }
    val searchResultIsEmpty = remember { mutableStateOf(searchResultIsEmptyAsState) }
    val searchWord = remember { mutableStateOf(viewModel.prevSearchWord) }
    val isClosed = remember { mutableStateOf(false) }
    val showFailDialog = remember { mutableStateOf(null as String?) }

    LaunchedEffect(searchResultItemsAsState) {
        searchResultItems.value = searchResultItemsAsState
    }

    LaunchedEffect(searchRecommendItemsAsState) {
        searchRecommendItems.value = searchRecommendItemsAsState
    }

    LaunchedEffect(searchWord.value) {
        if (searchWord.value.isEmpty()) {
            searchResultItems.value = null
        }
    }

    LaunchedEffect(key1 = loadFail) {
        if (loadFail != null) {
            showFailDialog.value = loadFail
        }
    }

    LaunchedEffect(key1 = searchResultIsEmptyAsState) {
        searchResultIsEmpty.value = searchResultIsEmptyAsState
    }

    if (searchRecommendItems.value == null && !isClosed.value) {
        viewModel.loadSearchRecommendItems()
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        val (topAppBar, searchResultView) = createRefs()

        SearchTopAppBar(
            listState = listScrollState,
            title = searchWord.value,
            closeClicked = {
                isClosed.value = true
                closeEvent.invoke()
            },
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(topAppBar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
        )

        LazyColumn(
            modifier = Modifier
                .constrainAs(searchResultView) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(topAppBar.bottom)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                },
            contentPadding = PaddingValues(
                top = 16.dp, bottom = 70.dp
            ),
            state = listScrollState
        ) {
            item {
                SearchEditView(
                    goToSearch = { word ->
                        viewModel.loadSearchResult(word)
                        searchWord.value = word
                    },
                    currentSearchWord = searchWord.value
                )
            }

            // 검색 결과가 없는 경우
            if (searchWord.value.isNotEmpty() && searchResultIsEmpty.value == true) {
                item {
                    SearchResultBlankView(
                        modifier = Modifier.animateItemPlacement(),
                        searchWord = searchWord.value
                    )
                }
            }

            // 최근 검색어 + 추천 키워드 화면
            if (searchWord.value.isEmpty() || searchResultIsEmpty.value == true) {
                item {
                    searchRecommendItems.value?.let {
                        RecommendKeyWordView(
                            searchItems = it,
                            showOnlyRecommend = searchWord.value.isNotEmpty(),
                            itemClicked = { selectWord ->
                                searchWord.value = selectWord
                                viewModel.loadSearchResult(selectWord)
                            },
                            recentItemDelete = { deleteItem ->
                                viewModel.deleteRecentWord(deleteItem)
                            }
                        )
                    }
                }
            }

            // 검색 결과
            if (searchResultIsEmpty.value == false) {
                item {
                    searchResultItems.value?.let {
                        SearchResultView(
                            resultItems = it,
                            modifier = Modifier.animateItemPlacement(),
                            goToEvent = searchEvent,
                            actionEvent = { event ->
                                when (event) {
                                    is SearchActionEvent.RequestFollow -> {
                                        viewModel.requestFollow(event.userId, event.alreadyFollowed)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    if (showFailDialog.value != null) {
        val message =
            if (showFailDialog.value.isNullOrBlank()) stringResource(CommonR.string.retryDesc) else showFailDialog.value
        ApiFailDialog(
            title = stringResource(id = R.string.searchLoadFail),
            message = message,
            dismissEvent = {
                showFailDialog.value = null
                closeEvent()
            })
    }
}
