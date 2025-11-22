package com.zinc.waver.ui_feed

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.zinc.waver.ui.design.theme.Gray2
import com.zinc.waver.ui.util.WaverLoading
import com.zinc.waver.ui_feed.models.FeedClickEvent
import com.zinc.waver.ui_feed.models.FeedLoadStatus
import com.zinc.waver.ui_feed.models.UIFeedInfo
import com.zinc.waver.ui_feed.models.UIFeedKeyword
import com.zinc.waver.ui_feed.viewModel.FeedViewModel
import com.zinc.waver.ui_common.R as CommonR

@Composable
fun FeedScreen(goToBucket: (String, String) -> Unit) {

    val context = LocalContext.current

    val viewModel: FeedViewModel = hiltViewModel()
    val loadStatusAsState by viewModel.loadStatusEvent.observeAsState()
    val feedKeyWords by viewModel.feedKeyWords.observeAsState()
    val feedItems by viewModel.feedItems.observeAsState()

    Log.e("ayhan", "feedItems : $feedItems")

    var loadStatus by remember {
        mutableStateOf(loadStatusAsState)
    }

    LaunchedEffect(loadStatusAsState) {
        loadStatus = loadStatusAsState
    }

    LaunchedEffect(Unit) {
        viewModel.loadFeedItems()
    }

    FeedScreen(
        loadStatus = loadStatus ?: FeedLoadStatus.None,
        feedList = feedItems,
        keywordList = feedKeyWords,
        updateLoadStatus = {
            loadStatus = it
        },
        pullToRefreshEvent = {
            viewModel.loadFeedItems()
        },
        loadNextPageEvent = {

        },
        feedClickEvent = { event ->
            when (event) {
                is FeedClickEvent.GoToBucket -> {
                    goToBucket(event.bucketId, event.userId)
                }

                is FeedClickEvent.Like -> {
                    viewModel.saveBucketLike(event.id)
                }

                is FeedClickEvent.Scrap -> {
                    viewModel.copyOtherBucket(event.id)
                }
            }
        },
        keywordSaved = {
            viewModel.savedKeywordList(it)
        }
    )

    if (loadStatus == FeedLoadStatus.ToastFail) {
        Toast.makeText(
            context,
            CommonR.string.requestFailDesc,
            Toast.LENGTH_SHORT
        ).show()
        loadStatus = FeedLoadStatus.None
    } else if (loadStatus == FeedLoadStatus.CopySuccess) {
        Toast.makeText(
            context,
            CommonR.string.bucketCopySucceedToast,
            Toast.LENGTH_SHORT
        ).show()
        loadStatus = FeedLoadStatus.None
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun FeedScreen(
    loadStatus: FeedLoadStatus,
    feedList: List<UIFeedInfo>?,
    keywordList: List<UIFeedKeyword>?,
    pullToRefreshEvent: () -> Unit,
    loadNextPageEvent: () -> Unit,
    feedClickEvent: (FeedClickEvent) -> Unit,
    keywordSaved: (List<String>) -> Unit,
    updateLoadStatus: (FeedLoadStatus) -> Unit
) {

    val pullRefreshState = rememberPullRefreshState(
        refreshing = loadStatus == FeedLoadStatus.RefreshLoading,
        onRefresh = {
            pullToRefreshEvent()
        }
    )

    Box(
        modifier = Modifier
            .background(color = Gray2)
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .pullRefresh(pullRefreshState)

    ) {
        if (keywordList == null && feedList == null) {
            FeedScreenLoading()
        } else if (keywordList?.isNotEmpty() == true && feedList == null) {
            if (loadStatus is FeedLoadStatus.LoadFail) {
                if (loadStatus.hasData) {
                    updateLoadStatus(FeedLoadStatus.ToastFail)
                } else {
                    FeedBlankView()
                }
            } else {
                FeedKeywordsLayer(keywords = keywordList, recommendClicked = { list ->
                    keywordSaved(list)
                })
            }
        } else {
            FeedLayer(
                feedClicked = feedClickEvent,
                feedItems = feedList.orEmpty(),
                showPageLoading = loadStatus == FeedLoadStatus.PagingLoading,
                showLoadFail = loadStatus is FeedLoadStatus.LoadFail,
                loadNextPage = loadNextPageEvent
            )
        }

        if (loadStatus == FeedLoadStatus.KeywordLoading) {
            WaverLoading()
        }

        PullRefreshIndicator(
            refreshing = loadStatus == FeedLoadStatus.RefreshLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Preview
@Composable
private fun FeedScreenPreview1() {
    val mockFeedList = buildList {
        repeat(10) {
            add(
                UIFeedInfo(
                    bucketId = "1",
                    writerId = "11",
                    profileImage = null,
                    badgeImage = "",
                    titlePosition = "나는야 주스될거야",
                    nickName = "Sean Barker",
                    images = listOf(),
                    isProcessing = true,
                    title = "dolorum",
                    liked = false,
                    likeCount = 7367,
                    commentCount = 3546,
                    isScraped = false
                )
            )
        }
    }

    FeedScreen(
        loadNextPageEvent = {},
        pullToRefreshEvent = {},
        loadStatus = FeedLoadStatus.None,
        updateLoadStatus = {},
        feedList = mockFeedList,
        keywordList = emptyList(),
        feedClickEvent = { },
        keywordSaved = {}
    )
}

@Preview
@Composable
private fun FeedScreenPreview2() {
    val keyword = mutableListOf(UIFeedKeyword(id = "1", keyword = "여행가자"))
    FeedScreen(
        loadNextPageEvent = {},
        pullToRefreshEvent = {},
        loadStatus = FeedLoadStatus.None,
        updateLoadStatus = {},
        feedList = emptyList(),
        keywordList = keyword,
        feedClickEvent = { },
        keywordSaved = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun FeedScreenPreview3() {
    val keyword = mutableListOf(UIFeedKeyword(id = "1", keyword = "여행가자"))
    FeedScreen(
        loadNextPageEvent = {},
        pullToRefreshEvent = {},
        loadStatus = FeedLoadStatus.LoadFail(false),
        updateLoadStatus = {},
        feedList = emptyList(),
        keywordList = keyword,
        feedClickEvent = { },
        keywordSaved = {}
    )
}


@Preview(showBackground = true)
@Composable
private fun FeedScreenPreview4() {
    FeedScreen(
        goToBucket = { _, _ -> }
    )
}

