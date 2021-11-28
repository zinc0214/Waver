package com.zinc.berrybucket.compose.ui.my

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.Gray2
import com.zinc.berrybucket.compose.theme.Gray4
import com.zinc.berrybucket.compose.theme.Gray8
import com.zinc.berrybucket.compose.ui.component.BucketCard
import com.zinc.berrybucket.compose.ui.component.FilterAndSearchImageView
import com.zinc.berrybucket.model.*

@Composable
fun AllBucketLayer(
    allBucketInfo: AllBucketList,
    clickEvent: (MyClickEvent) -> Unit
) {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AllBucketTopView(
                modifier = Modifier,
                allBucketInfo = allBucketInfo,
                clickEvent = clickEvent
            )
            Spacer(modifier = Modifier.height(16.dp))
            AllBucketListView(allBucketInfo.bucketList)
        }
    }
}

@Composable
fun AllBucketTopView(
    modifier: Modifier = Modifier,
    allBucketInfo: AllBucketList,
    clickEvent: (MyClickEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Gray2)
    ) {
        BucketStateView(
            modifier = Modifier
                .padding(top = 24.dp, start = 22.dp)
                .align(Alignment.CenterVertically),
            proceedingBucketCount = allBucketInfo.proceedingBucketCount,
            succeedBucketCount = allBucketInfo.succeedBucketCount
        )

        FilterAndSearchImageView(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxWidth(),
            clickEvent = clickEvent,
            tabType = TabType.ALL
        )
    }
}


@Composable
private fun BucketStateView(
    modifier: Modifier = Modifier,
    proceedingBucketCount: String,
    succeedBucketCount: String
) {
    val proceedingText = stringResource(R.string.proceedingText) + " " + proceedingBucketCount
    val succeedingText = stringResource(R.string.succeedText) + " " + succeedBucketCount
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        Text(text = proceedingText, fontSize = 13.sp, color = Gray8)
        Divider(
            color = Gray4,
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .height(10.dp)
                .width(1.dp)
                .align(Alignment.CenterVertically)
        )
        Text(text = succeedingText, fontSize = 13.sp, color = Gray8)
    }
}

@Composable
private fun AllBucketListView(bucketList: List<BucketInfoSimple>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        bucketList.forEach { bucket ->
            BucketCard(
                itemInfo = bucket,
                bucketType = BucketType.BASIC,
                animFinishEvent = {}
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}