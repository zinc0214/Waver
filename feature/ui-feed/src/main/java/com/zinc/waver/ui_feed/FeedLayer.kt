package com.zinc.waver.ui_feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray2
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_feed.models.FeedClickEvent
import com.zinc.waver.ui_feed.models.UIFeedInfo

@Composable
fun FeedLayer(
    modifier: Modifier = Modifier,
    feedItems: List<UIFeedInfo>,
    showPageLoading: Boolean,
    loadNextPage: () -> Unit,
    showLoadFail: Boolean,
    feedClicked: (FeedClickEvent) -> Unit
) {

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(Gray2)
            .padding(horizontal = 16.dp)
    ) {

        TitleView(
            modifier = Modifier
                .padding(top = 24.dp)
                .padding(horizontal = 12.dp)
        )

        if (feedItems.isNotEmpty()) {
            FeedListView(
                modifier = Modifier
                    .padding(top = 24.dp),
                feedItems = feedItems,
                feedClicked = feedClicked
            )
        } else if (showLoadFail) {
            FeedBlankView(modifier = Modifier.padding(top = 220.dp))
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
                    isScraped = false
                )
            )
        }
    }
    FeedLayer(
        feedItems = feedList,
        showPageLoading = false,
        loadNextPage = {},
        feedClicked = {},
        showLoadFail = false
    )
}

@Preview(showBackground = true)
@Composable
private fun BlankPreview() {
    FeedLayer(
        feedItems = emptyList(),
        showPageLoading = false,
        loadNextPage = {},
        feedClicked = {},
        showLoadFail = true
    )
}