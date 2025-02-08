package com.zinc.waver.ui_my.screen.dday

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zinc.waver.model.MyPagerClickEvent
import com.zinc.waver.model.MyTabType.ALL
import com.zinc.waver.model.MyTabType.DDAY
import com.zinc.waver.ui.design.theme.Gray7
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_my.R
import com.zinc.waver.ui_my.SimpleBucketListView
import com.zinc.waver.ui_my.view.FilterAndSearchImageView
import com.zinc.waver.ui_my.viewModel.MyViewModel

@Composable
fun DdayBucketLayer(
    modifier: Modifier,
    viewModel: MyViewModel,
    clickEvent: (MyPagerClickEvent) -> Unit,
    updateScrollable: (Boolean) -> Unit,
    _isFilterUpdated: Boolean
) {

    val dDayBucketListAsState by viewModel.ddayBucketList.observeAsState()
    val isNeedToUpdate by viewModel.isNeedToUpdate.observeAsState()
    val filterLoadFinishedAsState by viewModel.ddayFilterLoadFinished.observeAsState()

    val bucketInfo = remember {
        mutableStateOf(dDayBucketListAsState)
    }
    val isFilterUpdated = remember {
        mutableStateOf(_isFilterUpdated)
    }

    LaunchedEffect(Unit) {
        viewModel.needToReload(true)
        updateScrollable(dDayBucketListAsState?.bucketList?.isNotEmpty() == true)
    }

    LaunchedEffect(key1 = isNeedToUpdate, block = {
        Log.e("ayhan", "isNeedToUpdate : ${isNeedToUpdate}")

        if (isNeedToUpdate == true) {
            viewModel.needToReload(false)
            viewModel.loadDdayBucketFilter()
            isFilterUpdated.value = false
            // 값 초기화
        }
    })

    LaunchedEffect(key1 = dDayBucketListAsState, block = {
        bucketInfo.value = dDayBucketListAsState
        updateScrollable(dDayBucketListAsState?.bucketList?.isNotEmpty() == true)
    })

    LaunchedEffect(key1 = filterLoadFinishedAsState) {
        Log.e("ayhan", "filterLoadFinishedAsState : $filterLoadFinishedAsState")
        if (filterLoadFinishedAsState == true) {
            viewModel.loadDdayBucketList()
            // isFilterDialogShown.value = false
        }
    }

    if (isFilterUpdated.value != _isFilterUpdated) {
        isFilterUpdated.value = _isFilterUpdated
        if (isFilterUpdated.value) {
            viewModel.loadDdayBucketFilter()
            Log.e("ayhan", "isFilterUpdated")
        }
    }

    bucketInfo.value?.let {
        Column(modifier = modifier) {
            DdayFilterAndSearchImageView(clickEvent = clickEvent)

            if (it.bucketList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                SimpleBucketListView(
                    it.bucketList.toMutableStateList(), DDAY, true,
                    itemClicked = {
                        clickEvent.invoke(MyPagerClickEvent.GoTo.BucketItemClicked(it))
                    },
                    achieveClicked = {
                        clickEvent.invoke(MyPagerClickEvent.AchieveBucketClicked(it))
                    }
                )
            } else {
                MyText(
                    text = stringResource(R.string.ddayEmptyGuide),
                    textAlign = TextAlign.Center,
                    color = Gray7,
                    fontSize = dpToSp(15.dp),
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 94.dp, horizontal = 26.dp)
                )
            }
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
        tabType = ALL
    )
}