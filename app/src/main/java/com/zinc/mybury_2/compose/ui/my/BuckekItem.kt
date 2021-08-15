package com.zinc.mybury_2.compose.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zinc.domain.models.BucketInfo
import com.zinc.mybury_2.compose.theme.BaseTheme

@Composable
fun BucketItemView(itemInfo: BucketInfo) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
    ) {
        Text(
            text = itemInfo.title
        )


    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    BaseTheme {
        BucketItemView(
            itemInfo = BucketInfo(
                id = "1234",
                title = "버킷리스트 아이템",
                memo = " ",
                isOpen = true,
                currentCount = 0
            )
        )
    }
}