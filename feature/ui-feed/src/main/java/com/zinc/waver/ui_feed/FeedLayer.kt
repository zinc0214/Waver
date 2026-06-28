package com.zinc.waver.ui_feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray2
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.presentation.screen.ads.NativeAdSlot
import com.zinc.waver.ui.presentation.screen.ads.rememberKeyedNativeAdCache
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_feed.models.FeedClickEvent
import com.zinc.waver.ui_feed.models.UIFeedInfo
import kotlinx.coroutines.flow.collectLatest

@Composable
fun FeedLayer(
    modifier: Modifier = Modifier,
    feedItems: List<UIFeedInfo>,
    pageEndIndices: List<Int>,
    hasNextPage: Boolean,
    showPageLoading: Boolean,
    loadNextPage: () -> Unit,
    showLoadFail: Boolean,
    feedClicked: (FeedClickEvent) -> Unit
) {

    val listState = rememberLazyListState()
    val pageEndIndexSet = remember(pageEndIndices) { pageEndIndices.toSet() }

    val adCache = rememberKeyedNativeAdCache()
    val boundaryBucketIds = remember(pageEndIndices, feedItems) {
        pageEndIndices.mapNotNull { feedItems.getOrNull(it)?.bucketId }
    }
    LaunchedEffect(boundaryBucketIds) {
        boundaryBucketIds.forEach { adCache.ensureLoaded(it) }
        adCache.retainOnly(boundaryBucketIds.toSet())
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Gray2)
            .padding(horizontal = 16.dp),
        state = listState
    ) {

        item {
            TitleView(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .padding(horizontal = 12.dp)
            )
        }

        if (feedItems.isNotEmpty()) {
            item(key = "feed_top_spacer") { Spacer(modifier = Modifier.height(24.dp)) }
            feedItems.forEachIndexed { index, feed ->
                item(key = "feed_${feed.bucketId}", contentType = "feed_card") {
                    FeedCardView(
                        feedInfo = feed,
                        clickEvent = feedClicked
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                if (index in pageEndIndexSet) {
                    item(key = "ad_${feed.bucketId}", contentType = "feed_ad") {
                        NativeAdSlot(cache = adCache, key = feed.bucketId)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
            item(key = "feed_footer") {
                Spacer(modifier = Modifier.height(48.dp))
                if (showPageLoading) {
                    PageLoadingView()
                }
            }
        } else if (showLoadFail) {
            item {
                FeedBlankView(modifier = Modifier.padding(top = 220.dp))
            }
        }
    }

    PagingTrigger(
        listState = listState,
        hasNextPage = hasNextPage,
        showPageLoading = showPageLoading,
        loadNextPage = loadNextPage
    )
}

@Composable
private fun PagingTrigger(
    listState: LazyListState,
    hasNextPage: Boolean,
    showPageLoading: Boolean,
    loadNextPage: () -> Unit
) {
    LaunchedEffect(listState, hasNextPage, showPageLoading) {
        snapshotFlow {
            val layout = listState.layoutInfo
            val lastVisible = layout.visibleItemsInfo.lastOrNull()?.index ?: -1
            lastVisible to layout.totalItemsCount
        }.collectLatest { (lastVisible, totalCount) ->
            if (!hasNextPage || showPageLoading || totalCount <= 0) return@collectLatest
            // 마지막 2개 이내로 보이면 다음 페이지 요청
            if (lastVisible >= totalCount - 3) {
                loadNextPage()
            }
        }
    }
}

@Composable
private fun TitleView(modifier: Modifier = Modifier) {
    MyText(
        text = stringResource(id = R.string.feedContentTitle),
        color = Gray10,
        fontSize = dpToSp(24.dp),
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Preview
@Composable
private fun FeedLayerPreview() {
    val feedList = buildList {
        repeat(10) {
            add(
                UIFeedInfo(
                    isMine = false,
                    bucketId = it.toString(),
                    writerId = it.toString() + 100,
                    profileImage = null,
                    badgeImage = "consul",
                    titlePosition = "dictumst",
                    nickName = "Cherry McConnell",
                    images = listOf(),
                    isProcessing = false,
                    title = "wisi",
                    liked = false,
                    likeCount = 7133,
                    commentCount = 7659,
                    isScrapAvailable = false
                )
            )
        }
    }
    FeedLayer(
        feedItems = feedList,
        pageEndIndices = listOf(4, feedList.lastIndex),
        showPageLoading = false,
        loadNextPage = {},
        feedClicked = {},
        showLoadFail = false,
        hasNextPage = true
    )
}

@Preview(showBackground = true)
@Composable
private fun BlankPreview() {
    FeedLayer(
        feedItems = emptyList(),
        pageEndIndices = emptyList(),
        showPageLoading = false,
        loadNextPage = {},
        feedClicked = {},
        showLoadFail = true,
        hasNextPage = true
    )
}
