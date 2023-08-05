package com.zinc.berrybucket.ui_my.dday

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.model.MyPagerClickEvent
import com.zinc.berrybucket.model.MyTabType.ALL
import com.zinc.berrybucket.model.MyTabType.DDAY
import com.zinc.berrybucket.model.parseToUI
import com.zinc.berrybucket.ui_my.SimpleBucketListView
import com.zinc.berrybucket.ui_my.view.FilterAndSearchImageView
import com.zinc.berrybucket.ui_my.viewModel.MyViewModel

@Composable
fun DdayBucketLayer(
    viewModel: MyViewModel,
    clickEvent: (MyPagerClickEvent) -> Unit
) {

    viewModel.loadDdayBucketList()
    val dDayBucketList by viewModel.ddayBucketList.observeAsState()

    dDayBucketList?.let {
        Column {
            DdayFilterAndSearchImageView(clickEvent = clickEvent)
            Spacer(modifier = Modifier.height(16.dp))
            SimpleBucketListView(
                it.bucketList.parseToUI(), DDAY(), true,
                itemClicked = {
                    clickEvent.invoke(MyPagerClickEvent.BucketItemClicked(it))
                },
                nestedScrollInterop = null
            )
        }
    }
}

@Composable
private fun DdayFilterAndSearchImageView(
    modifier: Modifier = Modifier,
    clickEvent: (MyPagerClickEvent) -> Unit
) {
    FilterAndSearchImageView(
        modifier = modifier
            .fillMaxWidth(),
        clickEvent = { clickEvent.invoke(it) },
        tabType = ALL()
    )
}