package com.zinc.berrybucket.ui.presentation.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.R
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Gray2
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.common.models.FeedInfo


@Composable
fun FeedLayer(feedItems: List<FeedInfo>) {

    Scaffold { _ ->
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Gray2)
                .padding(horizontal = 16.dp)
                .padding(top = 40.dp)
                .verticalScroll(rememberScrollState())

        ) {

            TitleView(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .padding(horizontal = 12.dp)
            )

            FeedListView(
                modifier = Modifier
                    .padding(top = 24.dp),
                feedItems = feedItems
            )
        }
    }
}

@Composable
private fun TitleView(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = R.string.feedContentTitle),
        color = Gray10,
        fontSize = dpToSp(24.dp),
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}