package com.zinc.waver.ui_feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray2
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_feed.models.FeedClickEvent
import com.zinc.waver.ui_feed.models.UIFeedInfo

@Composable
fun FeedLayer(
    modifier: Modifier,
    feedItems: List<UIFeedInfo>,
    feedClicked: (FeedClickEvent) -> Unit
) {

    Column(
        modifier = modifier
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
            feedItems = feedItems,
            feedClicked = feedClicked
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