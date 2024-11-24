package com.zinc.waver.ui_feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray2
import com.zinc.waver.ui.design.theme.Gray6
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_feed.models.FeedClickEvent
import com.zinc.waver.ui_feed.viewModel.FeedViewModel
import com.zinc.waver.ui_common.R as CommonR

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FeedLayer(
    modifier: Modifier,
    viewModel: FeedViewModel,
    feedClicked: (FeedClickEvent) -> Unit
) {
    val state by viewModel.isLoading.observeAsState()
    val feedItemsAsState by viewModel.feedItems.observeAsState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state == true,
        onRefresh = viewModel::loadFeedKeyWords
    )

    var feedItems by remember {
        mutableStateOf(feedItemsAsState)
    }

    var isLoading by remember {
        mutableStateOf(state)
    }

    LaunchedEffect(Unit) {
        viewModel.loadFeedItems()
    }

    LaunchedEffect(feedItemsAsState) {
        feedItems = feedItemsAsState
    }

    LaunchedEffect(state) {
        isLoading = state
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray2)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .pullRefresh(pullRefreshState)

    ) {

        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {

            TitleView(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .padding(horizontal = 12.dp)
            )

            if (feedItems.isNullOrEmpty() && isLoading == false) {
                BlankView(modifier = Modifier.padding(top = 220.dp))
            } else if (!feedItems.isNullOrEmpty()) {
                FeedListView(
                    modifier = Modifier
                        .padding(top = 24.dp),
                    feedItems = feedItems.orEmpty(),
                    feedClicked = feedClicked
                )
            }
        }

        PullRefreshIndicator(
            refreshing = isLoading == true,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
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

@Composable
private fun BlankView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(CommonR.drawable.btn_60_blank), contentDescription = null)
        MyText(
            stringResource(R.string.feedBlankGuide),
            textAlign = TextAlign.Center,
            color = Gray6,
            fontSize = dpToSp(16.dp)
        )
    }
}

@Preview
@Composable
private fun BlankPreview() {
    BlankView()
}