package com.zinc.waver.ui.presentation.screen.blank

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import com.zinc.waver.ui.design.theme.Gray3
import com.zinc.waver.ui.design.theme.Gray4
import com.zinc.waver.ui.presentation.component.IconButton
import com.zinc.waver.ui_common.R

@Composable
fun MyTopLayerLoading() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shimmer(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Box(
                modifier = Modifier
                    .background(shape = CircleShape, color = Gray3)
                    .size(80.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_icon_blank),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )
            }

            Image(
                painter = painterResource(id = R.drawable.blank_badge),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 60.dp)
                    .size(32.dp, 36.dp)
                    .align(Alignment.Center)
            )
        }

        Box(
            modifier = Modifier
                .padding(top = 16.dp)
                .size(width = 196.dp, height = 22.dp)
                .background(color = Gray3)
        )

        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .size(width = 196.dp, height = 22.dp)
                .background(color = Gray3)
        )

        Row(modifier = Modifier.padding(top = 45.dp)) {
            Box(
                modifier = Modifier
                    .size(width = 36.dp, height = 22.dp)
                    .background(color = Gray3)
            )

            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(width = 28.dp, height = 22.dp)
                    .background(color = Gray3)
            )

            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(width = 36.dp, height = 22.dp)
                    .background(color = Gray3)
            )

            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(width = 28.dp, height = 22.dp)
                    .background(color = Gray3)
            )
        }
    }
}

@Composable
fun AllBucketTopLoading(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(top = 20.dp, start = 16.dp)
                .shimmer()
                .size(width = 113.dp, height = 24.dp)
                .background(color = Gray3)
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = modifier.padding(top = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = {},
                modifier = Modifier.size(32.dp),
                image = R.drawable.btn_32_filter,
                contentDescription = null,
            )

            Divider(
                color = Gray4,
                modifier = Modifier
                    .padding(start = 7.5.dp, end = 7.5.dp)
                    .height(16.dp)
                    .width(1.dp)
                    .align(Alignment.CenterVertically)
            )

            IconButton(
                onClick = {
                },
                modifier = Modifier.size(32.dp),
                image = R.drawable.btn_32_search,
                contentDescription = null
            )
        }
    }
}

@Composable
fun AllBucketItemLoading(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .shimmer()
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(3) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(height = 64.dp)
                    .background(color = Gray3)
            ) {
                Text(text = "")
            }
        }
    }
}

@Preview
@Composable
private fun MyTopLayerLoadingPreview() {
    MyTopLayerLoading()
}

@Preview
@Composable
private fun AllBucketTopLoadingPreview() {
    AllBucketTopLoading(
        modifier = Modifier
    )
}

@Preview
@Composable
private fun AllBucketItemLoadingPreview() {
    AllBucketItemLoading()
}