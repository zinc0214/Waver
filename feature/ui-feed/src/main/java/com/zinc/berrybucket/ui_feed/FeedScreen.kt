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
    val feedItems by viewModel.feedItems.observeAsState()
    val feedKeyWords by viewModel.feedKeyWords.observeAsState()

    val showLoadFailDialog = remember {
        mutableStateOf(false)
    }
    val isAlreadyKeywordSelected = remember {
        mutableStateOf(false)
    }

    if (feedItems.isNullOrEmpty()) {
        viewModel.loadFeedItems()
    }

    LaunchedEffect(key1 = isKeyWordSelected) {
        if (isKeyWordSelected == false) {
            viewModel.loadFeedKeyWords()
        }
        isAlreadyKeywordSelected.value = isKeyWordSelected == true
    }

    LaunchedEffect(key1 = apiLoadFail) {
        showLoadFailDialog.value = apiLoadFail == true
    }

    Scaffold { padding ->
        rememberSystemUiController().setSystemBarsColor(Gray2)
        if (isAlreadyKeywordSelected.value) {
            feedItems?.let {
                FeedLayer(
                    modifier = Modifier.padding(padding),
                    feedItems = it,
                    feedClicked = { id -> feedClicked(id) }
                )
            }
        } else {
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