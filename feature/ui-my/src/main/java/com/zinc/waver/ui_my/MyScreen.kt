package com.zinc.waver.ui_my

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue.Expanded
import androidx.compose.material.ModalBottomSheetValue.HalfExpanded
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.zinc.waver.model.HomeItemSelected
import com.zinc.waver.model.MyPagerClickEvent
import com.zinc.waver.model.MyTabType
import com.zinc.waver.model.MyTabType.ALL
import com.zinc.waver.model.MyTabType.CATEGORY
import com.zinc.waver.model.MyTabType.DDAY
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray3
import com.zinc.waver.ui.design.theme.Gray6
import com.zinc.waver.ui.design.util.isFirstItemVisible
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.WaverLoading
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_my.model.MyTopEvent
import com.zinc.waver.ui_my.screen.all.AllBucketLayer
import com.zinc.waver.ui_my.screen.category.screen.CategoryLayer
import com.zinc.waver.ui_my.screen.dday.DdayBucketLayer
import com.zinc.waver.ui_my.viewModel.MyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyScreen(
    itemSelected: (HomeItemSelected) -> Unit,
    bottomSheetClicked: (BottomSheetScreenType) -> Unit,
    myTopEvent: (MyTopEvent) -> Unit,
    goToCategoryEdit: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val viewModel: MyViewModel = hiltViewModel()

    val profileInfoAsState by viewModel.profileInfo.observeAsState()

    val showLoadingAsState by viewModel.showLoading.observeAsState()

    val profileInfo = remember {
        mutableStateOf(profileInfoAsState)
    }

    val configuration = LocalConfiguration.current

    val pagerHeight = configuration.screenHeightDp.dp - 128.dp

    val showLoading = remember { mutableStateOf(false) }

    val tabItems = MyTabType.values()
    val pagerState = rememberPagerState(pageCount = { tabItems.size })

    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                viewModel.loadProfile()
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(key1 = profileInfoAsState) {
        profileInfo.value = profileInfoAsState
    }

    LaunchedEffect(showLoadingAsState) {
        Log.e("ayhan", "showLoadingAsState : $showLoadingAsState")
        showLoading.value = showLoadingAsState == true
        Log.e("ayhan", "showLoading : $showLoading")
    }

    ////////////////////////////
    ////BottomSheet////////////
    ///////////////////////////
    val isFilterUpdated = remember {
        mutableStateOf(false)
    }
    val myTabType = remember {
        mutableIntStateOf(0)
    }
    val bottomSheetScaffoldState = rememberModalBottomSheetState(
        initialValue = Hidden, skipHalfExpanded = true
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

    val scrollState = rememberLazyListState()

    var isListScrollable by remember { mutableStateOf(false) }

    LaunchedEffect(bottomSheetScaffoldState.currentValue) {
        when (bottomSheetScaffoldState.currentValue) {
            Hidden -> {
                bottomSheetClicked.invoke(BottomSheetScreenType.MyBucketFilterScreen(false))
            }

            Expanded, HalfExpanded -> {
                bottomSheetClicked.invoke(BottomSheetScreenType.MyBucketFilterScreen(true))
            }
        }
    }

    LaunchedEffect(scrollState.isFirstItemVisible) {
        isListScrollable = !scrollState.isFirstItemVisible
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetScaffoldState,
        sheetContent = {
            Box(
                Modifier
                    .defaultMinSize(minHeight = 1.dp)
            ) {
                FilterBottomView(
                    tab = if (myTabType.intValue == 0) ALL else DDAY,
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
        profileInfo.value?.let {
            LazyColumn(modifier = Modifier.statusBarsPadding(), state = scrollState) {
                item {
                    MyTopLayer(profileInfo = profileInfo.value) {
                        myTopEvent(it)
                    }
                }
                stickyHeader("stickyHeader") {
                    MyTabLayer(tabItems, pagerState, coroutineScope)
                }

                item {
                    MyViewPager(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(pagerHeight),
                        pagerState = pagerState,
                        viewModel = viewModel,
                        isFilterUpdated = isFilterUpdated.value,
                        itemSelected = itemSelected,
                        bottomSheetClicked = {
                            bottomSheetClicked(it)

                            if (it is BottomSheetScreenType.MyBucketFilterScreen) {
                                myTabType.intValue = pagerState.currentPage
                                isNeedToBottomSheetOpen.invoke(it.needToShown)
                            }
                        },
                        goToCategoryEdit = goToCategoryEdit,
                        coroutineScope = coroutineScope,
                        isListScrollable = isListScrollable
                    )
                }
            }
        }
    }

    if (showLoading.value) {
        WaverLoading()
    }
}


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

    Column {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
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
        Divider(modifier = Modifier.fillMaxWidth(), color = Gray3)
    }

}

@Composable
fun MyViewPager(
    pagerState: PagerState,
    viewModel: MyViewModel,
    coroutineScope: CoroutineScope,
    isListScrollable: Boolean,
    isFilterUpdated: Boolean,
    itemSelected: (HomeItemSelected) -> Unit,
    bottomSheetClicked: (BottomSheetScreenType) -> Unit,
    goToCategoryEdit: () -> Unit,
    modifier: Modifier = Modifier
) {

    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        pageSize = PageSize.Fill,
        key = { count -> count },
        pageContent = { page ->
            when (page) {
                0 -> {
                    AllBucketLayer(
                        modifier = modifier.verticalScroll(
                            state = rememberScrollState(),
                            enabled = isListScrollable
                        ),
                        viewModel = viewModel,
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
                                            BottomSheetScreenType.MyBucketSearchScreen(
                                                selectTab = ALL, viewModel = viewModel
                                            )
                                        )
                                    }
                                }

                                is MyPagerClickEvent.BottomSheet.FilterClicked -> {
                                    bottomSheetClicked.invoke(
                                        BottomSheetScreenType.MyBucketFilterScreen(it.isOpened)
                                    )
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
                    CategoryLayer(
                        modifier = modifier.verticalScroll(
                            state = rememberScrollState(),
                            enabled = isListScrollable
                        ),
                        clickEvent = {
                            when (it) {
                                is MyPagerClickEvent.GoTo.CategoryEditClicked -> {
                                    goToCategoryEdit()
                                }

                                is MyPagerClickEvent.BottomSheet.SearchClicked -> {
                                    coroutineScope.launch {
                                        bottomSheetClicked.invoke(
                                            BottomSheetScreenType.MyBucketSearchScreen(
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
                    DdayBucketLayer(
                        modifier = modifier.verticalScroll(
                            state = rememberScrollState(),
                            enabled = isListScrollable
                        ),
                        viewModel = viewModel,
                        _isFilterUpdated = isFilterUpdated,
                        clickEvent = {
                            when (it) {
                                is MyPagerClickEvent.GoTo.BucketItemClicked -> {
                                    itemSelected.invoke(HomeItemSelected.GoToDetailHomeItem(it.info))
                                }

                                is MyPagerClickEvent.BottomSheet.SearchClicked -> {
                                    coroutineScope.launch {
                                        bottomSheetClicked.invoke(
                                            BottomSheetScreenType.MyBucketSearchScreen(
                                                selectTab = DDAY,
                                                viewModel = viewModel
                                            )
                                        )
                                    }
                                }

                                is MyPagerClickEvent.BottomSheet.FilterClicked -> {
                                    bottomSheetClicked.invoke(
                                        BottomSheetScreenType.MyBucketFilterScreen(it.isOpened)
                                    )
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
    tabWidths: MutableList<Dp>,
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