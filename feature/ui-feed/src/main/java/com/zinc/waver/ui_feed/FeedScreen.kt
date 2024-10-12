package com.zinc.waver.ui_feed

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.zinc.waver.ui.design.theme.Gray2
import com.zinc.waver.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.waver.ui_feed.models.FeedClickEvent
import com.zinc.waver.ui_feed.viewModel.FeedViewModel
import com.zinc.waver.ui_common.R as CommonR

@Composable
fun FeedScreen(goToBucket: (String, String) -> Unit) {

    val context = LocalContext.current

    val viewModel: FeedViewModel = hiltViewModel()
    val isKeyWordSelected by viewModel.isKeyWordSelected.observeAsState()
    val apiLoadFail by viewModel.loadFail.observeAsState()
    val feedItemsAsState by viewModel.feedItems.observeAsState()
    val feedKeyWords by viewModel.feedKeyWords.observeAsState()
    val likeFailAsState by viewModel.likeFail.observeAsState()

    val showLoadFailDialog = remember {
        mutableStateOf(false)
    }
    val isAlreadyKeywordSelected = remember {
        mutableStateOf(false)
    }
    val showLikeFailToast = remember {
        mutableStateOf(false)
    }
    val feedItems = remember {
        mutableStateOf(feedItemsAsState)
    }


    LaunchedEffect(key1 = isKeyWordSelected) {
        if (isKeyWordSelected == false) {
            viewModel.loadFeedKeyWords()
        } else {
            viewModel.loadFeedItems()
        }
        isAlreadyKeywordSelected.value = isKeyWordSelected == true
    }

    LaunchedEffect(key1 = apiLoadFail) {
        showLoadFailDialog.value = apiLoadFail == true
    }

    LaunchedEffect(key1 = likeFailAsState) {
        showLikeFailToast.value = likeFailAsState == true
    }

    LaunchedEffect(key1 = feedItemsAsState) {
        if (feedItemsAsState == null) {
            viewModel.checkSavedKeyWords()
        } else {
            feedItems.value = feedItemsAsState
        }
    }

    Scaffold { padding ->
        rememberSystemUiController().setSystemBarsColor(Gray2)
        if (isAlreadyKeywordSelected.value) {
            feedItems.value?.let {
                FeedLayer(
                    modifier = Modifier.padding(padding),
                    feedItems = it,
                    feedClicked = { event ->
                        when (event) {
                            is FeedClickEvent.GoToBucket -> goToBucket(event.bucketId, event.userId)
                            is FeedClickEvent.Like -> {
                                viewModel.saveBucketLike(event.id)
                            }

                            is FeedClickEvent.Scrap -> TODO()
                        }
                    }
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

    if (showLikeFailToast.value) {
        Toast.makeText(
            context,
            CommonR.string.requestFailDesc,
            Toast.LENGTH_SHORT
        ).show()
        showLikeFailToast.value = false
    }
}