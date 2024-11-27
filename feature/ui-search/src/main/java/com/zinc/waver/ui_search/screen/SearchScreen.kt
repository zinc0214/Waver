package com.zinc.waver.ui_search.screen

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
import androidx.compose.runtime.setValue
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

@Composable
fun SearchScreen(
    closeEvent: () -> Unit,
    searchEvent: (SearchGoToEvent) -> Unit
) {
    val viewModel: SearchViewModel = hiltViewModel()
    val searchRecommendItems by viewModel.searchRecommendItems.observeAsState()
    val searchResultItemsAsState by viewModel.searchResultItems.observeAsState()
    val loadFail by viewModel.loadFail.observeAsState()

    val listScrollState = rememberLazyListState()
    var searchResultItems by remember { mutableStateOf(searchResultItemsAsState) }
    var searchWord by remember { mutableStateOf(viewModel.prevSearchWord) }
    var isClosed by remember { mutableStateOf(false) }
    var showFailDialog by remember { mutableStateOf(null as String?) }

    LaunchedEffect(Unit) {
        viewModel.loadSearchRecommendItems()
    }

    LaunchedEffect(searchResultItemsAsState) {
        searchResultItems = searchResultItemsAsState
    }

    LaunchedEffect(searchWord) {
        if (searchWord.isEmpty()) {
            searchResultItems = null
        }
    }

    LaunchedEffect(key1 = loadFail) {
        if (loadFail != null) {
            showFailDialog = loadFail
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        val (topAppBar, searchResultView) = createRefs()

        SearchTopAppBar(
            listState = listScrollState,
            title = searchWord,
            closeClicked = {
                isClosed = true
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
                        searchWord = word
                    },
                    currentSearchWord = searchWord
                )
            }

            // 검색 결과가 없는 경우
            if (searchWord.isNotEmpty() && searchResultItems?.hasItems() == false) {
                item {
                    SearchResultBlankView(
                        modifier = Modifier.animateItem(),
                        searchWord = searchWord
                    )
                }
            }

            // 최근 검색어 + 추천 키워드 화면
            if (searchWord.isEmpty() || searchResultItems?.hasItems() == false) {
                item {
                    searchRecommendItems?.let {
                        RecommendKeyWordView(
                            searchItems = it,
                            showOnlyRecommend = searchWord.isNotEmpty(),
                            itemClicked = { selectWord ->
                                searchWord = selectWord
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
            if (searchResultItems?.hasItems() == true) {
                item {
                    searchResultItems?.let {
                        SearchResultView(
                            resultItems = it,
                            modifier = Modifier.animateItem(),
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

    if (showFailDialog != null) {
        val message =
            if (showFailDialog.isNullOrBlank()) stringResource(CommonR.string.retryDesc) else showFailDialog
        ApiFailDialog(
            title = stringResource(id = R.string.searchLoadFail),
            message = message,
            dismissEvent = {
                showFailDialog = null
                closeEvent()
            })
    }
}
