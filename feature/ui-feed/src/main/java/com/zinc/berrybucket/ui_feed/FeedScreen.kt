package com.zinc.berrybucket.ui_feed

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.zinc.berrybucket.ui.design.theme.Gray2
import com.zinc.berrybucket.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.berrybucket.ui_feed.viewModel.FeedViewModel

@Composable
fun FeedScreen(feedClicked: (String) -> Unit) {

    val viewModel: FeedViewModel = hiltViewModel()
    val isKeyWordSelected by viewModel.isKeyWordSelected.observeAsState()
    val apiLoadFail by viewModel.loadFail.observeAsState()
    val savedKeywordSucceed by viewModel.savedKeywordSucceed.observeAsState()

    val showLoadFailDialog = remember {
        mutableStateOf(false)
    }

    if (isKeyWordSelected == null) {
        viewModel.loadKeyWordSelected()
    }

    LaunchedEffect(key1 = apiLoadFail) {
        showLoadFailDialog.value = apiLoadFail == true
    }

    LaunchedEffect(key1 = savedKeywordSucceed) {
        if (savedKeywordSucceed == true) {
            viewModel.setKeyWordSelected()
        } else if (savedKeywordSucceed == false) {
            showLoadFailDialog.value = true
        }
    }

    Scaffold { padding ->
        rememberSystemUiController().setSystemBarsColor(Gray2)
        if (isKeyWordSelected == true) {
            val feedItems by viewModel.feedItems.observeAsState()
            if (feedItems == null) {
                viewModel.loadFeedItems()
            }
            feedItems?.let {
                FeedLayer(
                    modifier = Modifier.padding(padding),
                    feedItems = it,
                    feedClicked = { id -> feedClicked(id) }
                )
            }
        } else {
            viewModel.loadFeedKeyWords()
            val feedKeyWords by viewModel.feedKeyWords.observeAsState()
            feedKeyWords?.let {
                FeedKeywordsLayer(keywords = it, recommendClicked = { list ->
                    viewModel.savedKeywordList(list)
                })
            }
        }
    }

    if (showLoadFailDialog.value) {
        ApiFailDialog(
            title = stringResource(id = R.string.feedLoadFailTitle),
            message = stringResource(id = R.string.feedLoadFailContent)
        ) {
            if (isKeyWordSelected == true) {
                viewModel.loadFeedItems()
            } else {
                viewModel.loadFeedKeyWords()
            }
            showLoadFailDialog.value = false
        }
    }
}