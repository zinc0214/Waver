package com.zinc.waver.ui_my.screen.all

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.waver.model.AllBucketList
import com.zinc.waver.model.MyPagerClickEvent
import com.zinc.waver.model.MyTabType.ALL
import com.zinc.waver.ui.design.theme.Gray2
import com.zinc.waver.ui.design.theme.Gray4
import com.zinc.waver.ui.design.theme.Gray7
import com.zinc.waver.ui.design.theme.Gray8
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_common.R.string
import com.zinc.waver.ui_my.R
import com.zinc.waver.ui_my.SimpleBucketListView
import com.zinc.waver.ui_my.view.FilterAndSearchImageView
import com.zinc.waver.ui_my.viewModel.MyViewModel

@Composable
fun AllBucketLayer(
    modifier: Modifier,
    viewModel: MyViewModel,
    clickEvent: (MyPagerClickEvent) -> Unit,
    _isFilterUpdated: Boolean
) {

    val allBucketInfoAsState by viewModel.allBucketItem.observeAsState()
    val ddayShowPrefAsState by viewModel.showDdayView.observeAsState()
    val isNeedToUpdate by viewModel.isNeedToUpdate.observeAsState()
    val filterLoadFinishedAsState by viewModel.allFilterLoadFinished.observeAsState()

    var bucketInfo by remember("bucketInfo") {
        mutableStateOf(allBucketInfoAsState)
    }
    val ddayShow = remember {
        mutableStateOf(ddayShowPrefAsState)
    }
    val isFilterUpdated = remember {
        mutableStateOf(_isFilterUpdated)
    }

    LaunchedEffect(Unit) {
        viewModel.needToReload(true)
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
        bucketInfo = allBucketInfoAsState
        Log.e("ayhan", "bucketInfo : $bucketInfo")
    })

    LaunchedEffect(key1 = isNeedToUpdate, block = {
        Log.e("ayhan", "isNeedToUpdate : $isNeedToUpdate")

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

    bucketInfo?.let { it ->
        Column(modifier.background(Gray2)) {
            AllBucketTopView(
                modifier = Modifier,
                allBucketInfo = it,
                clickEvent = { event ->
                    clickEvent(event)

                    if (event is MyPagerClickEvent.BottomSheet.FilterClicked) {
                        isFilterUpdated.value = false
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (it.bucketList.isNotEmpty()) {
                SimpleBucketListView(
                    bucketList = it.bucketList,
                    tabType = ALL,
                    showDday = ddayShow.value ?: true,
                    itemClicked = {
                        clickEvent.invoke(MyPagerClickEvent.GoTo.BucketItemClicked(it))
                    },
                    achieveClicked = {
                        viewModel.achieveBucket(it, ALL)
                    })
            } else {
                BlankView(modifier = Modifier.fillMaxWidth())
            }
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
        stringResource(string.proceedingText) + " " + proceedingBucketCount
    val succeedingText =
        stringResource(string.succeedText) + " " + succeedBucketCount
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        MyText(text = proceedingText, fontSize = dpToSp(13.dp), color = Gray8)
        HorizontalDivider(
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

@Composable
private fun BlankView(modifier: Modifier = Modifier) {
    Box(modifier) {
        MyText(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Main4)) {
                    append(stringResource(id = R.string.allHasNoBucketListDesc1))
                }
                withStyle(style = SpanStyle(color = Gray7)) {
                    append(stringResource(id = R.string.allHasNoBucketListDesc2))
                }
            },
            fontSize = dpToSp(15.dp),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 26.dp)
                .padding(bottom = 110.dp, top = 86.dp)
        )
    }
}

@Preview
@Composable
private fun BlankPreview() {
    BlankView(modifier = Modifier)
}