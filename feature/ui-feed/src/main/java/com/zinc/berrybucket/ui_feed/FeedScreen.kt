package com.zinc.berrybucket.ui_feed

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.zinc.berrybucket.ui.design.theme.Gray2
import com.zinc.berrybucket.ui_feed.viewModel.FeedViewModel

@Composable
fun FeedScreen(feedClicked: (String) -> Unit) {

    val viewModel: FeedViewModel = hiltViewModel()
    val isKeyWordSelected by viewModel.isKeyWordSelected.observeAsState()

    if (isKeyWordSelected == null) {
        viewModel.loadKeyWordSelected()
    }

    Scaffold { padding ->
        rememberSystemUiController().setSystemBarsColor(Gray2)
        if (isKeyWordSelected != null && isKeyWordSelected == true) {
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
                FeedKeywordsLayer(keywords = it, recommendClicked = {
                    viewModel.setKeyWordSelected()
                })
            }
        }
    }

}