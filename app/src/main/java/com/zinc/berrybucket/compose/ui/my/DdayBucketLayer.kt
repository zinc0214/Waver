package com.zinc.berrybucket.compose.ui.my

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.compose.ui.component.BucketCard
import com.zinc.berrybucket.compose.ui.component.FilterAndSearchImageView
import com.zinc.berrybucket.model.BucketInfoSimple
import com.zinc.berrybucket.model.BucketType
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
            DdayBucketListView(dDayBucketList.bucketList)
        }
    }
}

@Composable
private fun DdayFilterAndSearchImageView(modifier: Modifier = Modifier) {
    FilterAndSearchImageView(
        modifier = modifier
            .fillMaxWidth(),
        filterClicked = {},
        searchClicked = {},
        tabType = TabType.ALL
    )
}

@Composable
private fun DdayBucketListView(bucketList: List<BucketInfoSimple>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        bucketList.forEach { bucket ->
            BucketCard(
                itemInfo = bucket,
                bucketType = BucketType.D_DAY,
                animFinishEvent = {}
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}