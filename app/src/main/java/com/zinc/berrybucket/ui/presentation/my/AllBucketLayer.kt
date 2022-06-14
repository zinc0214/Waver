package com.zinc.berrybucket.ui.presentation.my

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.AllBucketList
import com.zinc.berrybucket.model.MyPagerClickEvent
import com.zinc.berrybucket.model.MyTabType
import com.zinc.berrybucket.ui.compose.theme.Gray4
import com.zinc.berrybucket.ui.compose.theme.Gray8
import com.zinc.berrybucket.ui.presentation.common.BucketListView
import com.zinc.berrybucket.ui.presentation.common.FilterAndSearchImageView
import com.zinc.berrybucket.ui.presentation.my.viewModel.MyViewModel

@Composable
fun AllBucketLayer(
    viewModel: MyViewModel,
    clickEvent: (MyPagerClickEvent) -> Unit
) {

    viewModel.loadAllBucketList()
    val allBucketInfo by viewModel.allBucketItem.observeAsState()

    allBucketInfo?.let {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AllBucketTopView(
                modifier = Modifier,
                allBucketInfo = it,
                clickEvent = clickEvent
            )
            Spacer(modifier = Modifier.height(16.dp))
            BucketListView(it.bucketList, MyTabType.ALL, itemClicked = {
                clickEvent.invoke(MyPagerClickEvent.BucketItemClicked(it))
            })
        }
    }
}

@Composable
fun AllBucketTopView(
    modifier: Modifier = Modifier,
    allBucketInfo: AllBucketList,
    clickEvent: (MyPagerClickEvent) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        BucketStateView(
            modifier = Modifier
                .padding(top = 16.dp, start = 22.dp)
                .align(Alignment.CenterVertically),
            proceedingBucketCount = allBucketInfo.processingCount,
            succeedBucketCount = allBucketInfo.succeedCount
        )

        FilterAndSearchImageView(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxWidth(),
            clickEvent = clickEvent,
            tabType = MyTabType.ALL
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