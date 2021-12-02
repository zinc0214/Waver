package com.zinc.berrybucket.compose.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.compose.theme.BaseTheme
import com.zinc.berrybucket.compose.ui.component.ImageViewPagerOutSideIndicator
import com.zinc.berrybucket.compose.ui.component.ScrollableAppBar

@Composable
fun DetailLayer() {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 56.dp),
            state = rememberLazyListState()
        ) {
            //content goes here
        }

        ScrollableAppBar(
            modifier = Modifier.align(Alignment.Center),
            contents = {
                ImageViewPagerOutSideIndicator(imageList = listOf("A", "B", "C"))
            }
        )
    }
}

@Preview
@Composable
private fun DetailLayerTest() {
    BaseTheme {
        DetailLayer()
    }
}