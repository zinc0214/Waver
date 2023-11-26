package com.zinc.berrybucket.ui_my

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.zinc.berrybucket.model.HomeItemSelected
import com.zinc.berrybucket.model.MyPagerClickEvent
import com.zinc.berrybucket.model.MyTabType
import com.zinc.berrybucket.model.MyTabType.ALL
import com.zinc.berrybucket.model.MyTabType.CATEGORY
import com.zinc.berrybucket.model.MyTabType.DDAY
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Gray6
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_my.model.MyTopEvent
import com.zinc.berrybucket.ui_my.screen.all.AllBucketLayer
import com.zinc.berrybucket.ui_my.screen.category.screen.CategoryLayer
import com.zinc.berrybucket.ui_my.screen.dday.DdayBucketLayer
import com.zinc.berrybucket.ui_my.viewModel.MyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun MyScreen(
    itemSelected: (HomeItemSelected) -> Unit,
    bottomSheetClicked: (BottomSheetScreenType) -> Unit,
    myTopEvent: (MyTopEvent) -> Unit,
    goToCategoryEdit: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val viewModel: MyViewModel = hiltViewModel()

    viewModel.loadProfile()
    val profileInfo by viewModel.profileInfo.observeAsState()

    val tabItems = MyTabType.values()
    val pagerState = rememberPagerState(pageCount = { tabItems.size })

    val nestedScrollInterop = rememberNestedScrollInteropConnection()


    ////////////////////////////
    ////BottomSheet////////////
    ///////////////////////////
    val isFilterUpdated = remember {
        mutableStateOf(false)
    }
    val myTabType = remember {
        mutableStateOf(0)
    }
    val bottomSheetScaffoldState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = true
    )
    val isNeedToBottomSheetOpen: (Boolean) -> Unit = {
        coroutineScope.launch {
            if (it) {
                bottomSheetScaffoldState.show()
            } else {
                bottomSheetScaffoldState.hide()
            }
        }
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetScaffoldState,
        sheetContent = {
            Box(Modifier.defaultMinSize(minHeight = 1.dp)) {
                FilterBottomView(
                    tab = if (myTabType.value == 0) ALL else DDAY,
                    viewModel = viewModel,
                    isNeedToUpdated = {
                        isNeedToBottomSheetOpen.invoke(false)
                        isFilterUpdated.value = it
                    }
                )
            }

        },
        sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
    ) {
        profileInfo?.let {
            rememberSystemUiController().setSystemBarsColor(Gray1)
            CollapsingToolbarScaffold(
                modifier = Modifier.fillMaxSize(),
                state = rememberCollapsingToolbarScaffoldState(),
                scrollStrategy = ScrollStrategy.EnterAlways,
                toolbar = {
                    Column {
                        MyTopLayer(profileInfo = profileInfo) {
                            myTopEvent(it)
                        }

                    }
                }
            ) {
                Column {
                    MyTabLayer(tabItems, pagerState, coroutineScope)
                    MyViewPager(
                        pagerState = pagerState,
                        viewModel = viewModel,
                        isFilterUpdated = isFilterUpdated.value,
                        itemSelected = itemSelected,
                        bottomSheetClicked = {
                            bottomSheetClicked(it)

                            if (it is BottomSheetScreenType.FilterScreen) {
                                myTabType.value = if (it.selectTab == ALL) 0 else 2
                                isNeedToBottomSheetOpen.invoke(true)

                            }
                        },
                        goToCategoryEdit = goToCategoryEdit,
                        coroutineScope = coroutineScope,
                        nestedScrollInterop = nestedScrollInterop
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyTabLayer(
    tabItems: List<MyTabType>, pagerState: PagerState, coroutineScope: CoroutineScope
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyViewPager(
    pagerState: PagerState,
    viewModel: MyViewModel,
    coroutineScope: CoroutineScope,
    nestedScrollInterop: NestedScrollConnection,
    isFilterUpdated: Boolean,
    itemSelected: (HomeItemSelected) -> Unit,
    bottomSheetClicked: (BottomSheetScreenType) -> Unit,
    goToCategoryEdit: () -> Unit,
) {

    HorizontalPager(
        modifier = Modifier,
        state = pagerState,
        pageSize = PageSize.Fill,
        key = { count -> count },
        pageContent = { page ->
            when (page) {
                0 -> {
                    AllBucketLayer(
                        viewModel = viewModel,
                        nestedScrollInterop = nestedScrollInterop,
                        _isFilterUpdated = isFilterUpdated,
                        clickEvent = {
                            when (it) {
                                is MyPagerClickEvent.GoTo.BucketItemClicked -> {
                                    itemSelected.invoke(
                                        HomeItemSelected.GoToDetailHomeItem(
                                            it.info
                                        )
                                    )
                                }

                                is MyPagerClickEvent.BottomSheet.SearchClicked -> {
                                    coroutineScope.launch {
                                        bottomSheetClicked.invoke(
                                            BottomSheetScreenType.SearchScreen(
                                                selectTab = ALL, viewModel = viewModel
                                            )
                                        )
                                    }
                                }

                                is MyPagerClickEvent.BottomSheet.FilterClicked -> {
                                    coroutineScope.launch {
                                        bottomSheetClicked.invoke(
                                            BottomSheetScreenType.FilterScreen(ALL)
                                        )
                                    }
                                }

                                is MyPagerClickEvent.AchieveBucketClicked -> {
                                    viewModel.achieveBucket(it.id, ALL)
                                }

                                else -> {
                                    // Do Nothing
                                }
                            }
                        })
                }

                1 -> {
                    CategoryLayer(clickEvent = {
                        when (it) {
                            is MyPagerClickEvent.GoTo.CategoryEditClicked -> {
                                goToCategoryEdit()
                            }

                            is MyPagerClickEvent.BottomSheet.SearchClicked -> {
                                coroutineScope.launch {
                                    bottomSheetClicked.invoke(
                                        BottomSheetScreenType.SearchScreen(
                                            selectTab = CATEGORY, viewModel = viewModel
                                        )
                                    )
                                }
                            }

                            is MyPagerClickEvent.GoTo.CategoryItemClicked -> {
                                itemSelected.invoke(
                                    HomeItemSelected.GoToCategoryBucketList(
                                        it.info
                                    )
                                )
                            }

                            else -> {
                                // Do Nothing
                            }

                        }
                    })
                }

                2 -> {
                    DdayBucketLayer(viewModel = viewModel,
                        _isFilterUpdated = isFilterUpdated,
                        clickEvent = {
                            when (it) {
                                is MyPagerClickEvent.GoTo.BucketItemClicked -> {
                                    itemSelected.invoke(HomeItemSelected.GoToDetailHomeItem(it.info))
                                }

                                is MyPagerClickEvent.BottomSheet.SearchClicked -> {
                                    coroutineScope.launch {
                                        bottomSheetClicked.invoke(
                                            BottomSheetScreenType.SearchScreen(
                                                selectTab = DDAY,
                                                viewModel = viewModel
                                            )
                                        )
                                    }
                                }

                                is MyPagerClickEvent.BottomSheet.FilterClicked -> {
                                    coroutineScope.launch {
                                        bottomSheetClicked.invoke(
                                            BottomSheetScreenType.FilterScreen(DDAY)
                                        )
                                    }
                                }

                                is MyPagerClickEvent.AchieveBucketClicked -> {
                                    viewModel.achieveBucket(it.id, DDAY)
                                }

                                else -> {
                                    // Do Nothing
                                }
                            }
                        })
                }
            }
        }
    )
}

@Composable
private fun MyTab(
    mySection: MyTabType,
    isSelected: Boolean,
    tabWidths: SnapshotStateList<Dp>,
    currentIndex: Int,
    isClicked: (Int) -> Unit

) {
    val textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = dpToSp(15.dp))
    val density = LocalDensity.current

    Column(modifier = Modifier
        .padding(start = 12.dp, end = 12.dp)
        .clickable {
            isClicked(currentIndex)
        }) {
        MyText(text = stringResource(id = mySection.getTitle()),
            style = if (isSelected) textStyle.copy(color = Gray10)
            else textStyle.copy(color = Gray6),
            onTextLayout = { textLayoutResult ->
                tabWidths[currentIndex] =
                    with(density) { textLayoutResult.size.width.toDp() }
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