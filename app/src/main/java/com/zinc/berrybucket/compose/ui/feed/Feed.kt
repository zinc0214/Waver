package com.zinc.berrybucket.compose.ui.feed

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.berrybucket.presentation.feed.viewModel.FeedViewModel

@Composable
fun Feed() {

    val viewModel: FeedViewModel = hiltViewModel()
    viewModel.loadKeyWordSelected()

    val isKeyWordSelected by viewModel.isKeyWordSelected.observeAsState(false)

    Scaffold {
        if (isKeyWordSelected) {
            viewModel.loadFeedItems()
            val feedItems by viewModel.feedItems.observeAsState()
            feedItems?.let {
                FeedLayer(feedItems = it)
                viewModel.loadFeedKeyWords()
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