package com.zinc.berrybucket.ui_my.screen.all

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.zinc.berrybucket.model.AllBucketList
import com.zinc.berrybucket.model.MyPagerClickEvent
import com.zinc.berrybucket.model.MyTabType.ALL
import com.zinc.berrybucket.ui.design.theme.Gray2
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
    _isFilterUpdated: Boolean
) {

    val allBucketInfoAsState by viewModel.allBucketItem.observeAsState()
    val ddayShowPrefAsState by viewModel.showDdayView.observeAsState()
    val isNeedToUpdate by viewModel.isNeedToUpdate.observeAsState()
    val achieveSucceedAsState by viewModel.achieveSucceed.observeAsState()
    val filterLoadFinishedAsState by viewModel.allFilterLoadFinished.observeAsState()

    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    val bucketInfo = remember {
        mutableStateOf(allBucketInfoAsState)
    }
    val ddayShow = remember {
        mutableStateOf(ddayShowPrefAsState)
    }
    val isFilterUpdated = remember {
        mutableStateOf(_isFilterUpdated)
    }

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            Log.e("ayhan", "event  :$event")
            if (event == Lifecycle.Event.ON_CREATE) {
                viewModel.needToReload(true)
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(key1 = filterLoadFinishedAsState) {
        Log.e("ayhan", "filterLoadFinishedAsState : $filterLoadFinishedAsState")
        if (filterLoadFinishedAsState == true) {
            viewModel.loadAllBucketList()
            // isFilterDialogShown.value = false
        }
    }
    LaunchedEffect(key1 = ddayShowPrefAsState, block = {
        ddayShow.value = ddayShowPrefAsState
    })

    LaunchedEffect(key1 = allBucketInfoAsState, block = {
        bucketInfo.value = bucketInfo.value?.copy(bucketList = emptyList())
        bucketInfo.value = allBucketInfoAsState
    })

    LaunchedEffect(key1 = isNeedToUpdate, block = {
        Log.e("ayhan", "isNeedToUpdate : ${isNeedToUpdate}")

        if (isNeedToUpdate == true) {
            viewModel.needToReload(false)
            viewModel.loadAllBucketFilter()
            isFilterUpdated.value = false
            // 값 초기화
        }
    })

    if (isFilterUpdated.value != _isFilterUpdated) {
        isFilterUpdated.value = _isFilterUpdated
        if (isFilterUpdated.value) {
            viewModel.loadAllBucketFilter()
            // viewModel.loadAllBucketList()
            Log.e("ayhan", "isFilterUpdated")
        }
    }

    bucketInfo.value?.let {
        Column(Modifier.background(Gray2)) {
            AllBucketTopView(
                modifier = Modifier,
                allBucketInfo = it,
                clickEvent = {
                    clickEvent(it)

                    if (it is MyPagerClickEvent.BottomSheet.FilterClicked) {
                        isFilterUpdated.value = false
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            SimpleBucketListView(
                bucketList = it.bucketList,
                tabType = ALL,
                showDday = ddayShow.value ?: true,
                nestedScrollInterop = nestedScrollInterop,
                itemClicked = {
                    clickEvent.invoke(MyPagerClickEvent.GoTo.BucketItemClicked(it))
                },
                achieveClicked = {
                    viewModel.achieveBucket(it, ALL)
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
    var data by remember { mutableStateOf(allBucketInfo) }

    LaunchedEffect(allBucketInfo) {
        data = allBucketInfo
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        BucketStateView(
            modifier = Modifier
                .padding(top = 16.dp, start = 22.dp)
                .align(Alignment.CenterVertically),
            proceedingBucketCount = data.processingCount,
            succeedBucketCount = data.succeedCount
        )

        FilterAndSearchImageView(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxWidth(),
            clickEvent = clickEvent,
            tabType = ALL
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