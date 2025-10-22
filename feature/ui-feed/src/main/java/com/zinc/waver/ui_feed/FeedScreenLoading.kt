package com.zinc.waver.ui_feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray2
import com.zinc.waver.ui.design.theme.Gray4
import com.zinc.waver.ui.design.theme.Gray5
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.util.shadow

@Composable
fun FeedScreenLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(Gray2)
            .padding(horizontal = 16.dp)
    ) {
        MyText(
            text = stringResource(id = R.string.feedContentTitle),
            color = Gray10,
            fontSize = dpToSp(24.dp),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 24.dp)
                .padding(horizontal = 12.dp)
        )

        FeedItemLoading()
    }
}

@Composable
private fun FeedItemLoading() {
    Column(
        modifier = Modifier.padding(top = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        repeat(2) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        color = Gray5.copy(alpha = 0.2f),
                        offsetX = (0).dp,
                        offsetY = (0).dp,
                        blurRadius = 8.dp,
                    )
                    .background(color = Gray2, shape = RoundedCornerShape(8.dp))
                    .shimmer()
                    .padding(start = 16.dp, end = 33.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 33.dp, top = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = Gray4,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .size(32.dp)
                    )

                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .height(24.dp)
                            .background(color = Gray4)
                            .weight(1f)
                    ) {
                        Text(text = "")
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .background(color = Gray4)
                        .size(59.dp, 16.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 33.dp, top = 24.dp)
                        .background(color = Gray4)
                        .height(24.dp)
                )

                Row(modifier = Modifier.padding(top = 32.dp, bottom = 28.dp)) {
                    Box(
                        modifier = Modifier
                            .background(color = Gray4)
                            .size(60.dp, 16.dp)
                    )

                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .background(color = Gray4)
                            .size(60.dp, 16.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun FeedScreenLoadingPreview() {
    FeedScreenLoading()
}