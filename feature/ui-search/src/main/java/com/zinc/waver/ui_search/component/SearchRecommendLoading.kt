package com.zinc.waver.ui_search.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import com.zinc.waver.ui.design.theme.Gray3

@Composable
fun SearchRecommendLoading() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
    ) {
        Box(
            modifier = Modifier
                .shimmer()
                .size(width = 140.dp, height = 20.dp)
                .background(color = Gray3)
        )
        Box(
            modifier = Modifier
                .shimmer()
                .padding(top = 4.dp)
                .size(width = 140.dp, height = 20.dp)
                .background(color = Gray3)
        )

        Column(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .border(1.dp, Gray3, RoundedCornerShape(4.dp))
                .padding(12.dp)
                .shimmer()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .height(140.dp)
                    .background(color = Gray3)
            )
            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .size(width = 212.dp, height = 20.dp)
                    .background(color = Gray3)
            )
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(width = 212.dp, height = 20.dp)
                    .background(color = Gray3)
            )
        }


        Box(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .border(1.dp, Gray3, RoundedCornerShape(4.dp))
                .shimmer()

        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 22.dp, horizontal = 16.dp)
                    .size(width = 212.dp, height = 20.dp)
                    .background(color = Gray3)
            )
        }

        Box(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .border(1.dp, Gray3, RoundedCornerShape(4.dp))
                .shimmer()

        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 22.dp, horizontal = 16.dp)
                    .size(width = 212.dp, height = 20.dp)
                    .background(color = Gray3)
            )
        }
    }
}

@Preview
@Composable
private fun SearchRecommendLoadingPreview() {
    SearchRecommendLoading()
}