package com.zinc.berrybucket.compose.ui.my

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.Gray1
import com.zinc.berrybucket.compose.theme.Gray10
import com.zinc.berrybucket.compose.theme.Gray6
import com.zinc.berrybucket.compose.ui.BucketSelected
import com.zinc.berrybucket.model.ItemClicked
import com.zinc.berrybucket.model.MyClickEvent
import com.zinc.berrybucket.model.SearchClicked
import com.zinc.berrybucket.presentation.my.viewModel.MyViewModel
import com.zinc.berrybucket.ui.MyView
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun MyScreen(
    onBucketSelected: (BucketSelected) -> Unit,
) {
    val viewModel: MyViewModel = hiltViewModel()
    viewModel.loadProfile()
    val profileInfo by viewModel.profileInfo.observeAsState()

    val tabItems = MySections.values()
    val pagerState = rememberPagerState()

    profileInfo?.let { profile ->
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                MyView(context).apply {
                    setProfileInfo(profile)
                    setTabView(
                        tabItems = tabItems,
                        pagerState = pagerState,
                        viewModel = viewModel,
                        onBucketSelected = onBucketSelected
                    )
                }
            })
    }
}

enum class MySections(
    @StringRes val title: Int
) {
    ALL(title = R.string.allTab),
    CATEGORY(title = R.string.categoryTab),
    DDAY(title = R.string.ddayTab),
    CHALLENGE(title = R.string.challengeTab)
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MyTabLayer(
    tabItems: Array<MySections>,
    pagerState: PagerState
) {

    val coroutineScope = rememberCoroutineScope()
    val tabWidths = remember {
        val tabWidthStateList = mutableStateListOf<Dp>()
        repeat(tabItems.size) {
            tabWidthStateList.add(0.dp)
        }
        tabWidthStateList
    }

    LazyRow(
        modifier = Modifier
            .background(color = Gray1)
            .padding(start = 16.dp, top = 24.dp)
    ) {

        itemsIndexed(items = tabItems, itemContent = { index, tab ->
            MyTab(
                mySection = tab,
                isSelected = pagerState.currentPage == index,
                tabWidths = tabWidths,
                currentIndex = index,
                isClicked = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        })
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MyViewPager(
    tabItems: Array<MySections>,
    pagerState: PagerState,
    viewModel: MyViewModel,
    onBucketSelected: (BucketSelected) -> Unit
) {
    HorizontalPager(
        count = tabItems.size,
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) { page ->
        when (page) {
            0 -> {
                AllBucketLayer(viewModel = viewModel,
                    clickEvent = {
                        when (it) {
                            MyClickEvent.CloseClicked -> TODO()
                            MyClickEvent.FilterClicked -> TODO()
                            is ItemClicked -> {
                                onBucketSelected.invoke(BucketSelected.goToDetailBucket(it.info))
                            }
                            is SearchClicked -> TODO()
                        }
                    })
            }
            1 -> {
                CategoryLayer(viewModel = viewModel)
            }
            2 -> {
                DdayBucketLayer(
                    viewModel = viewModel,
                    clickEvent = {

                    })
            }
        }
    }
}

@Composable
private fun MyTab(
    mySection: MySections,
    isSelected: Boolean,
    tabWidths: SnapshotStateList<Dp>,
    currentIndex: Int,
    isClicked: (Int) -> Unit

) {
    val textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp)
    val density = LocalDensity.current

    Column(
        modifier = Modifier
            .padding(start = 12.dp, end = 12.dp)
            .clickable {
                isClicked(currentIndex)
            }
    ) {
        Text(
            text = stringResource(id = mySection.title),
            style = if (isSelected) textStyle.copy(color = Gray10)
            else textStyle.copy(color = Gray6),
            onTextLayout = { textLayoutResult ->
                tabWidths[currentIndex] = with(density) { textLayoutResult.size.width.toDp() }
            }
        )

        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(3.dp)
                .width(tabWidths[currentIndex])
                .background(if (isSelected) Gray10 else Color.Transparent)
        )
    }
}