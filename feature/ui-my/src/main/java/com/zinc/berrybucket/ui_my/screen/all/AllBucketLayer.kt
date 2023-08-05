package com.zinc.berrybucket.ui_my.screen.all

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.model.AllBucketList
import com.zinc.berrybucket.model.MyPagerClickEvent
import com.zinc.berrybucket.model.MyTabType.ALL
import com.zinc.berrybucket.ui.design.theme.Gray4
import com.zinc.berrybucket.ui.design.theme.Gray8
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_my.SimpleBucketListView
import com.zinc.berrybucket.ui_my.view.FilterAndSearchImageView
import com.zinc.berrybucket.ui_my.viewModel.MyViewModel

@Composable
fun AllBucketLayer(
    viewModel: MyViewModel,
    clickEvent: (MyPagerClickEvent) -> Unit,
    nestedScrollInterop: NestedScrollConnection,
) {

    val allBucketInfo by viewModel.allBucketItem.observeAsState()
    val ddayShowPref by viewModel.showDdayView.observeAsState()


    val bucketInfo = remember {
        mutableStateOf(allBucketInfo)
    }
    val ddayShow = remember {
        mutableStateOf(ddayShowPref)
    }

    LaunchedEffect(key1 = ddayShowPref, block = {
        ddayShow.value = ddayShowPref
    })

    LaunchedEffect(key1 = allBucketInfo, block = {
        bucketInfo.value = allBucketInfo
    })


    viewModel.loadBucketFilter()
    viewModel.loadAllBucketList()

    bucketInfo.value?.let {
        Column {
            AllBucketTopView(
                modifier = Modifier,
                allBucketInfo = it,
                clickEvent = clickEvent
            )
            Spacer(modifier = Modifier.height(16.dp))
            SimpleBucketListView(
                bucketList = it.bucketList,
                tabType = ALL(),
                showDday = ddayShow.value ?: true,
                nestedScrollInterop = nestedScrollInterop,
                itemClicked = {
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
            tabType = ALL()
        )
    }
}


@Composable
private fun BucketStateView(
    modifier: Modifier = Modifier,
    proceedingBucketCount: String,
    succeedBucketCount: String
) {
    val proceedingText =
        stringResource(com.zinc.berrybucket.ui_common.R.string.proceedingText) + " " + proceedingBucketCount
    val succeedingText =
        stringResource(com.zinc.berrybucket.ui_common.R.string.succeedText) + " " + succeedBucketCount
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