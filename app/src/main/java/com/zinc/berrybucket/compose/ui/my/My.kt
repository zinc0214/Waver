package com.zinc.berrybucket.compose.ui.my

import androidx.annotation.StringRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.Gray1
import com.zinc.berrybucket.compose.theme.Gray10
import com.zinc.berrybucket.compose.theme.Gray6
import com.zinc.berrybucket.presentation.my.viewModel.MyViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun My(
    modifier: Modifier = Modifier, key: String
) {
    val viewModel: MyViewModel = hiltViewModel()
    viewModel.loadProfile()
    val profileInfo by viewModel.profileInfo.observeAsState()

    val tabItems = MySections.values()
    val pagerState = rememberPagerState()

    Column(
        modifier = modifier
    ) {
        profileInfo?.let {
            MyTopLayer(profileInfo = it)
        }

        MyTabLayer(
            viewModel = viewModel,
            tabItems = tabItems,
            pagerState = pagerState
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
    tabItems: Array<MySections>,
    pagerState: PagerState
) {
    val coroutinScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val tabWidths = remember {
        val tabWidthStateList = mutableStateListOf<Dp>()
        repeat(tabItems.size) {
            tabWidthStateList.add(0.dp)
        }
        tabWidthStateList
    }

    Column {
        ScrollableTabRow (
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .customTabIndicatorOffset(
                            currentTabPosition = tabPositions[pagerState.currentPage],
                            tabWidth = tabWidths[pagerState.currentPage]
                        )
                        .height(3.dp)
                )
            },
            backgroundColor = Gray1,
            edgePadding = 0.dp
        ) {
            tabItems.forEachIndexed { index, mySections ->
                val textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Tab(
                    text = {
                        Text(
                            text = stringResource(id = mySections.title),
                            style = if (pagerState.currentPage == index) textStyle.copy(color = Gray10)
                            else textStyle.copy(color = Gray6),
                            onTextLayout = { textLayoutResult ->
                                tabWidths[index] =
                                    with(density) { textLayoutResult.size.width.toDp() }
                            }
                        )
                    },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutinScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }
        }

        HorizontalPager(
            count = tabItems.size,
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            when (page) {
                0 -> {
                    AllBucketLayer(viewModel = viewModel,
                        clickEvent = {

                        })
                }
                1 -> {
                    CategoryLayer(viewModel)
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
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
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