package com.zinc.berrybucket.compose.ui.my

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import com.zinc.berrybucket.compose.theme.Gray1
import com.zinc.berrybucket.compose.theme.Gray10
import com.zinc.berrybucket.compose.theme.Gray6
import com.zinc.berrybucket.compose.ui.BucketSelected
import com.zinc.berrybucket.model.BottomButtonClickEvent
import com.zinc.berrybucket.model.MyPagerClickEvent
import com.zinc.berrybucket.model.MySearchClickEvent
import com.zinc.berrybucket.model.MyTabType
import com.zinc.berrybucket.presentation.my.viewModel.MyViewModel
import com.zinc.berrybucket.ui.MyView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun MyScreen(
    onBucketSelected: (BucketSelected) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val viewModel: MyViewModel = hiltViewModel()
    viewModel.loadProfile()
    val profileInfo by viewModel.profileInfo.observeAsState()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    var currentBottomSheet: BottomSheetScreenType? by remember { mutableStateOf(null) }

    val tabItems = MyTabType.values()
    val pagerState = rememberPagerState()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            currentBottomSheet?.let {
                MyBottomSheetScreen(
                    currentScreen = it,
                    bottomSheetScaffoldState = bottomSheetScaffoldState,
                    coroutineScope = coroutineScope
                )
            }
        },
        sheetShape = if (currentBottomSheet is BottomSheetScreenType.FilterScreen)
            RoundedCornerShape(
                topEnd = 16.dp,
                topStart = 16.dp
            ) else RoundedCornerShape(0.dp),
        sheetPeekHeight = 1.dp,
        sheetGesturesEnabled = currentBottomSheet !is BottomSheetScreenType.SearchScreen
    ) {
        profileInfo?.let { profile ->
            AndroidView(modifier = Modifier.fillMaxSize(), factory = { context ->
                MyView(context).apply {
                    setProfileInfo(profile)
                    setTabView(tabItems = tabItems,
                        pagerState = pagerState,
                        viewModel = viewModel,
                        coroutineScope = coroutineScope,
                        onBucketSelected = onBucketSelected,
                        bottomSheetClicked = {
                            currentBottomSheet = it
                            coroutineScope.launch {
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            }
                        })
                }
            })
        }
    }


}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun MyTabLayer(
    tabItems: Array<MyTabType>, pagerState: PagerState, coroutineScope: CoroutineScope
) {

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
            MyTab(mySection = tab,
                isSelected = pagerState.currentPage == index,
                tabWidths = tabWidths,
                currentIndex = index,
                isClicked = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                })
        })
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MyViewPager(
    tabItems: Array<MyTabType>,
    pagerState: PagerState,
    viewModel: MyViewModel,
    coroutineScope: CoroutineScope,
    onBucketSelected: (BucketSelected) -> Unit,
    bottomSheetClicked: (BottomSheetScreenType) -> Unit
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
                AllBucketLayer(viewModel = viewModel, clickEvent = {
                    when (it) {
                        is MyPagerClickEvent.ItemClicked -> {
                            onBucketSelected.invoke(BucketSelected.goToDetailBucket(it.info))
                        }
                        is MyPagerClickEvent.SearchClicked -> {
                            coroutineScope.launch {
                                bottomSheetClicked.invoke(
                                    BottomSheetScreenType.SearchScreen(
                                        selectTab = MyTabType.ALL, viewModel = viewModel
                                    )
                                )
                            }
                        }
                        MyPagerClickEvent.FilterClicked -> {
                            coroutineScope.launch {
                                bottomSheetClicked.invoke(
                                    BottomSheetScreenType.FilterScreen(
                                        viewModel = viewModel
                                    )
                                )
                            }
                        }
                    }
                })
            }
            1 -> {
                CategoryLayer(viewModel = viewModel)
            }
            2 -> {
                DdayBucketLayer(viewModel = viewModel, clickEvent = {

                })
            }
        }
    }
}

@Composable
private fun MyTab(
    mySection: MyTabType,
    isSelected: Boolean,
    tabWidths: SnapshotStateList<Dp>,
    currentIndex: Int,
    isClicked: (Int) -> Unit

) {
    val textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp)
    val density = LocalDensity.current

    Column(modifier = Modifier
        .padding(start = 12.dp, end = 12.dp)
        .clickable {
            isClicked(currentIndex)
        }) {
        Text(text = stringResource(id = mySection.title),
            style = if (isSelected) textStyle.copy(color = Gray10)
            else textStyle.copy(color = Gray6),
            onTextLayout = { textLayoutResult ->
                tabWidths[currentIndex] = with(density) { textLayoutResult.size.width.toDp() }
            })

        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(3.dp)
                .width(tabWidths[currentIndex])
                .background(if (isSelected) Gray10 else Color.Transparent)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MyBottomSheetScreen(
    currentScreen: BottomSheetScreenType,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope,
) {
    when (currentScreen) {
        is BottomSheetScreenType.FilterScreen -> {
            FilterBottomView(
                viewModel = currentScreen.viewModel,
                bottomSheetScaffoldState = bottomSheetScaffoldState,
                coroutineScope = coroutineScope
            )
        }
        is BottomSheetScreenType.SearchScreen -> {
            SearchBottomView(
                tab = currentScreen.selectTab,
                viewModel = currentScreen.viewModel,
                bottomSheetScaffoldState = bottomSheetScaffoldState,
                coroutineScope = coroutineScope
            )
        }
        else -> {

        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SearchBottomView(
    tab: MyTabType,
    viewModel: MyViewModel,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope
) {


    MySearchBottomScreen(currentTabType = tab, clickEvent = {
        when (it) {
            MySearchClickEvent.CloseClicked -> {
                coroutineScope.launch {
                    if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                }
            }
            is MySearchClickEvent.ItemClicked -> TODO()
            is MySearchClickEvent.SearchClicked -> TODO()
        }
    }, searchWord = { tab, word ->

    }, result = viewModel.searchResult.observeAsState()
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun FilterBottomView(
    viewModel: MyViewModel,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope
) {
    MyFilterBottomScreen(viewModel = viewModel, clickEvent = {
        when (it) {
            BottomButtonClickEvent.LeftButtonClicked -> {
                coroutineScope.launch {
                    if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                }
            }
            BottomButtonClickEvent.RightButtonClicked -> TODO()
        }
    })

}

sealed class BottomSheetScreenType {
    data class SearchScreen(
        val selectTab: MyTabType, val viewModel: MyViewModel
    ) : BottomSheetScreenType()

    data class FilterScreen(
        val viewModel: MyViewModel
    ) : BottomSheetScreenType()
}