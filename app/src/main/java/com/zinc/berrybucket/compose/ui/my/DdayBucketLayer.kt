package com.zinc.berrybucket.compose.ui.my

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.compose.ui.component.BucketListView
import com.zinc.berrybucket.compose.ui.component.FilterAndSearchImageView
import com.zinc.berrybucket.model.DDayBucketList
import com.zinc.berrybucket.model.TabType


@Composable
fun DdayBucketLayer(dDayBucketList: DDayBucketList) {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            DdayFilterAndSearchImageView()
            Spacer(modifier = Modifier.height(16.dp))
            BucketListView(dDayBucketList.bucketList, TabType.D_DAY)
        }
    }
}

@Composable
private fun DdayFilterAndSearchImageView(modifier: Modifier = Modifier) {
    FilterAndSearchImageView(
        modifier = modifier
            .fillMaxWidth(),
        clickEvent = {},
        tabType = TabType.ALL
    )
}