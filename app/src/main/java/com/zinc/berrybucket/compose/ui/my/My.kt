package com.zinc.berrybucket.compose.ui.my

import androidx.annotation.StringRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.TabPosition
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.Gray10
import com.zinc.berrybucket.compose.theme.Gray6
import com.zinc.berrybucket.compose.ui.BucketSelected
import com.zinc.berrybucket.model.ItemClicked
import com.zinc.berrybucket.model.MyClickEvent
import com.zinc.berrybucket.model.SearchClicked
import com.zinc.berrybucket.presentation.my.viewModel.MyViewModel
import kotlinx.coroutines.launch


private val MaxTabOffset = 354.dp
private val MinTabOffset = 24.dp

@Composable
fun My(
    modifier: Modifier = Modifier,
    onBucketSelected: (BucketSelected) -> Unit,
    key: String
) {
    val viewModel: MyViewModel = hiltViewModel()
    viewModel.loadProfile()
    val profileInfo by viewModel.profileInfo.observeAsState()

    val scrollState = rememberScrollState(0)

    Box {
        profileInfo?.let {
            MyTopLayer(profileInfo = it,
                scrollProvider = {
                    scrollState.value
                })
        }
        MyTabLayer(
            viewModel = viewModel,
            scrollProvider = {
                scrollState.value
            },
            scrollState = scrollState,
            onBucketSelected = onBucketSelected
        )
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
private fun MyTabLayer(
    viewModel: MyViewModel,
    scrollProvider: () -> Int,
    scrollState: ScrollState,
    onBucketSelected: (BucketSelected) -> Unit
) {
    val pagerState = rememberPagerState()
    val tabItems = MySections.values()
    val coroutineScope = rememberCoroutineScope()
    val tabWidths = remember {
        val tabWidthStateList = mutableStateListOf<Dp>()
        repeat(tabItems.size) {
            tabWidthStateList.add(0.dp)
        }
        tabWidthStateList
    }

    val maxOffset = with(LocalDensity.current) { MaxTabOffset.toPx() }
    val minOffset = with(LocalDensity.current) { MinTabOffset.toPx() }

    Column(
        modifier = Modifier
            .offset {
                val scroll = scrollProvider()
                val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
                IntOffset(x = 0, y = offset.toInt())
            }
    ) {
        // 추후 수정 가능성이 있어 주석 처리
//        TabRow(
//            selectedTabIndex = pagerState.currentPage,
//            indicator = { tabPositions ->
//                TabRowDefaults.Indicator(
//                    Modifier
//                        .customTabIndicatorOffset(
//                            currentTabPosition = tabPositions[pagerState.currentPage],
//                            tabWidth = tabWidths[pagerState.currentPage]
//                        )
//                        .height(3.dp)
//                )
//            },
//            divider = {
//                TabRowDefaults.Divider(
//                    thickness = 0.dp,
//                    color = Color.Transparent
//                )
//            },
//            backgroundColor = Gray1,
//            modifier = Modifier.padding(0.dp).wrapContentHeight()
//        ) {
//            tabItems.forEachIndexed { index, mySections ->
//                val textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp)
//                Tab(
//                    text = {
//                        Text(
//                            text = stringResource(id = mySections.title),
//                            style = if (pagerState.currentPage == index) textStyle.copy(color = Gray10)
//                            else textStyle.copy(color = Gray6),
//                            onTextLayout = { textLayoutResult ->
//                                tabWidths[index] =
//                                    with(density) { textLayoutResult.size.width.toDp() }
//                            }
//                        )
//                    },
//                    selected = pagerState.currentPage == index,
//                    onClick = {
//                        coroutineScope.launch {
//                            pagerState.animateScrollToPage(index)
//                        }
//                    },
//                    modifier = Modifier.wrapContentHeight().padding(0.dp)
//                )
//            }
//        }

        LazyRow(modifier = Modifier.padding(start = 16.dp)) {
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

        HorizontalPager(
            count = tabItems.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
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

fun Modifier.customTabIndicatorOffset(
    currentTabPosition: TabPosition,
    tabWidth: Dp
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "customTabIndicatorOffset"
        value = currentTabPosition
    }
) {
    val currentTabWidth by animateDpAsState(
        targetValue = tabWidth,
        animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing)
    )
    val indicatorOffset by animateDpAsState(
        targetValue = ((currentTabPosition.left + currentTabPosition.right - tabWidth) / 2),
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
}