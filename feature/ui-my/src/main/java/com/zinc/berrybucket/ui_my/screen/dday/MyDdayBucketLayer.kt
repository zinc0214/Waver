package com.zinc.berrybucket.ui_my.screen.dday

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.model.MyPagerClickEvent
import com.zinc.berrybucket.model.MyTabType.ALL
import com.zinc.berrybucket.model.MyTabType.DDAY
import com.zinc.berrybucket.ui_my.SimpleBucketListView
import com.zinc.berrybucket.ui_my.view.FilterAndSearchImageView
import com.zinc.berrybucket.ui_my.viewModel.MyViewModel

@Composable
fun DdayBucketLayer(
    viewModel: MyViewModel,
    clickEvent: (MyPagerClickEvent) -> Unit
) {

    val dDayBucketListAsState by viewModel.ddayBucketList.observeAsState()
    val isPrefChangeAsState by viewModel.isNeedToUpdate.observeAsState()

    val bucketInfo = remember {
        mutableStateOf(dDayBucketListAsState)
    }

    LaunchedEffect(key1 = isPrefChangeAsState, block = {
        if (isPrefChangeAsState == true) {
            // 값 초기화
            viewModel.updatePrefChangeState(changed = false, isNeedClear = true)
            if (bucketInfo.value != null) {
                bucketInfo.value = null
            }
        }
    })

    LaunchedEffect(key1 = dDayBucketListAsState, block = {
        bucketInfo.value = dDayBucketListAsState
    })

    if (bucketInfo.value == null) {
        viewModel.loadDdayBucketFilter()
        viewModel.loadDdayBucketList()
    }


    bucketInfo.value?.let {
        Column {
            DdayFilterAndSearchImageView(clickEvent = clickEvent)
            Spacer(modifier = Modifier.height(16.dp))
            SimpleBucketListView(
                it.bucketList.toMutableStateList(), DDAY(), true,
                itemClicked = {
                    clickEvent.invoke(MyPagerClickEvent.BucketItemClicked(it))
                },
                achieveClicked = {
                    clickEvent.invoke(MyPagerClickEvent.AchieveBucketClicked(it))
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