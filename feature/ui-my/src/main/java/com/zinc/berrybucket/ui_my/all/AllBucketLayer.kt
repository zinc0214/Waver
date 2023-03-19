package com.zinc.berrybucket.ui_my.all

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.common.R
import com.zinc.berrybucket.model.AllBucketList
import com.zinc.berrybucket.model.MyPagerClickEvent
import com.zinc.berrybucket.model.MyTabType
import com.zinc.berrybucket.ui.design.theme.Gray4
import com.zinc.berrybucket.ui.design.theme.Gray8
import com.zinc.berrybucket.ui.presentation.common.MyText
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_my.FilterAndSearchImageView
import com.zinc.berrybucket.ui_my.SimpleBucketListView
import com.zinc.berrybucket.ui_my.viewModel.MyViewModel
import com.zinc.common.models.AllBucketListRequest
import com.zinc.common.models.AllBucketListSortType
import com.zinc.common.models.YesOrNo

@Composable
fun AllBucketLayer(
    viewModel: MyViewModel,
    clickEvent: (MyPagerClickEvent) -> Unit
) {

    viewModel.loadAllBucketList(
        allBucketListRequest = AllBucketListRequest(
            dDayBucketOnly = YesOrNo.N.name,
            isPassed = YesOrNo.N.name,
            isCompleted = YesOrNo.N.name,
            sort = AllBucketListSortType.CREATED
        )
    )
    val allBucketInfo by viewModel.allBucketItem.observeAsState()

    allBucketInfo?.let {
        Column {
            AllBucketTopView(
                modifier = Modifier,
                allBucketInfo = it,
                clickEvent = clickEvent
            )
            Spacer(modifier = Modifier.height(16.dp))
            SimpleBucketListView(it.bucketList, MyTabType.ALL, itemClicked = {
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
        MyText(text = proceedingText, fontSize = dpToSp(13.dp), color = Gray8)
        Divider(
            color = Gray4,
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .height(10.dp)
                .width(1.dp)
                .align(Alignment.CenterVertically)
        )
        MyText(text = succeedingText, fontSize = dpToSp(13.dp), color = Gray8)
    }
}