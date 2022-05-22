package com.zinc.berrybucket.compose.ui.my

import androidx.compose.foundation.background
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
import com.zinc.berrybucket.compose.theme.Gray2
import com.zinc.berrybucket.compose.theme.Gray4
import com.zinc.berrybucket.compose.theme.Gray8
import com.zinc.berrybucket.compose.ui.common.BucketListView
import com.zinc.berrybucket.compose.ui.common.FilterAndSearchImageView
import com.zinc.berrybucket.model.AllBucketList
import com.zinc.berrybucket.model.ItemClicked
import com.zinc.berrybucket.model.MyClickEvent
import com.zinc.berrybucket.model.TabType
import com.zinc.berrybucket.presentation.my.viewModel.MyViewModel

@Composable
fun AllBucketLayer(
    viewModel: MyViewModel,
    clickEvent: (MyClickEvent) -> Unit
) {

    viewModel.loadAllBucketList()
    val allBucketInfo by viewModel.allBucketItem.observeAsState()

    allBucketInfo?.let {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .background(Gray2)
        ) {
            AllBucketTopView(
                modifier = Modifier,
                allBucketInfo = it,
                clickEvent = clickEvent
            )
            Spacer(modifier = Modifier.height(16.dp))
            BucketListView(it.bucketList, TabType.ALL, itemClicked = {
                clickEvent.invoke(ItemClicked(it))
            })
            Spacer(modifier = Modifier.weight(1f))
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
        modifier = modifier
            .fillMaxWidth()
    ) {
        BucketStateView(
            modifier = Modifier
                .padding(top = 24.dp, start = 22.dp)
                .align(Alignment.CenterVertically),
            proceedingBucketCount = allBucketInfo.processingCount,
            succeedBucketCount = allBucketInfo.succeedCount
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