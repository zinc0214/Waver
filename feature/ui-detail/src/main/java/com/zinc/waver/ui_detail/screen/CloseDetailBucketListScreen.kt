package com.zinc.waver.ui_detail.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.zinc.waver.model.BucketDetailUiInfo
import com.zinc.waver.model.DetailAppBarClickEvent
import com.zinc.waver.model.DetailLoadFailStatus
import com.zinc.waver.model.LoadedImageInfo
import com.zinc.waver.model.SuccessButtonInfo
import com.zinc.waver.model.WriteTotalInfo
import com.zinc.waver.ui.design.theme.BaseTheme
import com.zinc.waver.ui.design.util.rememberScrollContext
import com.zinc.waver.ui.presentation.component.ImageViewPagerInsideIndicator
import com.zinc.waver.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.waver.ui.util.WaverLoading
import com.zinc.waver.ui_common.R
import com.zinc.waver.ui_detail.component.DetailDescView
import com.zinc.waver.ui_detail.component.DetailMemoView
import com.zinc.waver.ui_detail.component.DetailSuccessButtonView
import com.zinc.waver.ui_detail.component.DetailTopAppBar
import com.zinc.waver.ui_detail.component.GoalCountUpdateDialog
import com.zinc.waver.ui_detail.component.MyDetailAppBarMoreMenuDialog
import com.zinc.waver.ui_detail.model.GoalCountUpdateEvent
import com.zinc.waver.ui_detail.model.MyBucketMoreMenuEvent
import com.zinc.waver.ui_detail.model.OpenBucketDetailInternalEvent
import com.zinc.waver.ui_detail.model.toUpdateUiModel
import com.zinc.waver.ui_detail.viewmodel.DetailViewModel
import com.zinc.waver.util.createImageInfoWithPath

@Composable
fun CloseDetailScreen(
    detailId: String,
    goToUpdate: (WriteTotalInfo) -> Unit,
    backPress: () -> Unit
) {

    val context = LocalContext.current
    val viewModel: DetailViewModel = hiltViewModel()

    val vmDetailInfoAsState by viewModel.bucketBucketDetailUiInfo.observeAsState()
    val loadFailAsState by viewModel.loadFail.observeAsState()
    val showLoadingAsState by viewModel.showLoading.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getBucketDetail(detailId, "", true)
    }

    val detailInfo = remember { mutableStateOf(vmDetailInfoAsState) }
    val optionPopUpShowed = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val goalCountUpdatePopUpShowed = remember { mutableStateOf(false) } // 달성횟수 팝업 노출 여부
    val imageInfos = remember { mutableListOf<LoadedImageInfo>() }

    val loadFail = remember { mutableStateOf(loadFailAsState) }
    val showLoadFailDialog = remember { mutableStateOf(false) }
    val showLoading = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = vmDetailInfoAsState) {
        detailInfo.value = vmDetailInfoAsState
    }

    LaunchedEffect(key1 = loadFailAsState) {
        if (loadFailAsState != null) {
            loadFail.value = loadFailAsState
            showLoadFailDialog.value = true
        }
    }

    LaunchedEffect(showLoadingAsState) {
        showLoading.value = showLoadingAsState == true
    }

    detailInfo.value?.let { info ->
        val listScrollState = rememberLazyListState()
        val scrollContext = rememberScrollContext(listScrollState)
        val titlePosition = 0

        if (imageInfos.isEmpty()) {
            imageInfos.addAll(createImageInfoWithPath(context, info.imageInfo?.imageList.orEmpty()))
        }

        BaseTheme {
            Scaffold { padding ->

                if (optionPopUpShowed.value) {
                    MyDetailAppBarMoreMenuDialog(dismiss = {
                        optionPopUpShowed.value = false
                    }, event = {
                        if (it is OpenBucketDetailInternalEvent.BucketMore.My) {
                            when (it.event) {
                                MyBucketMoreMenuEvent.GoToEdit -> {
                                    optionPopUpShowed.value = false
                                    goToUpdate(info.toUpdateUiModel(imageInfos))
                                }

                                MyBucketMoreMenuEvent.GoToGoalUpdate -> {
                                    goalCountUpdatePopUpShowed.value = true
                                    optionPopUpShowed.value = false
                                }

                                MyBucketMoreMenuEvent.GoToDelete -> {
                                    optionPopUpShowed.value = false
                                    viewModel.deleteMyBucket()
                                }
                            }
                        }
                    })
                }

                if (goalCountUpdatePopUpShowed.value) {
                    GoalCountUpdateDialog(
                        currentCount = info.descInfo.goalCount.toString()
                    ) {
                        when (it) {
                            GoalCountUpdateEvent.Close -> {
                                goalCountUpdatePopUpShowed.value = false
                            }

                            is GoalCountUpdateEvent.CountUpdate -> {
                                // Todo : ViewModel Update!
                                viewModel.requestGoalCountUpdate(it.count)
                                goalCountUpdatePopUpShowed.value = false
                            }
                        }
                    }
                }


                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .statusBarsPadding()
                        .padding(padding)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = { optionPopUpShowed.value = false }
                        )
                ) {
                    DetailTopAppBar(
                        listState = listScrollState,
                        titlePosition = titlePosition,
                        title = info.descInfo.title,
                        clickEvent = {
                            when (it) {
                                DetailAppBarClickEvent.CloseClicked -> {
                                    backPress()
                                }

                                DetailAppBarClickEvent.MoreOptionClicked -> {
                                    optionPopUpShowed.value = true
                                }
                            }
                        }
                    )

                    ConstraintLayout(
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        val (contentView, floatingButtonView) = createRefs()

                        ContentView(
                            listState = listScrollState,
                            bucketDetailUiInfo = info,
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(contentView) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(parent.top)
                                    height = Dimension.fillToConstraints
                                })

                        if (info.canShowCompleteButton) {
                            DetailSuccessButtonView(
                                modifier = Modifier
                                    .constrainAs(floatingButtonView) {
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                        bottom.linkTo(parent.bottom)
                                    }
                                    .padding(bottom = if (scrollContext.isBottom) 28.dp else 0.dp),
                                successClicked = {
                                    viewModel.achieveMyBucket()
                                },
                                successButtonInfo = SuccessButtonInfo(
                                    goalCount = info.descInfo.goalCount,
                                    userCount = info.descInfo.userCount
                                ),
                                isWide = !scrollContext.isBottom
                            )
                        }
                    }
                }
            }
        }
    }

    if (showLoading.value) {
        WaverLoading()
    }

    if (showLoadFailDialog.value) {
        val title = when (loadFail.value) {
            DetailLoadFailStatus.AchieveFail -> stringResource(id = R.string.achieveBucketFail)
            DetailLoadFailStatus.LoadFail -> stringResource(id = R.string.loadBucketFail)
            else -> stringResource(id = R.string.apiFailTitle)
        }
        ApiFailDialog(
            title = title,
            message = stringResource(id = R.string.apiFailMessage)
        ) {
            when (loadFail.value) {
                DetailLoadFailStatus.AchieveFail -> viewModel.achieveMyBucket()
                DetailLoadFailStatus.LoadFail -> {
                    viewModel.getBucketDetail(detailId, "", true)
                }

                else -> {
                    // Do Nothing
                }
            }
            showLoadFailDialog.value = false
        }
    }
}


@Composable
private fun ContentView(
    modifier: Modifier,
    listState: LazyListState,
    bucketDetailUiInfo: BucketDetailUiInfo
) {

    LazyColumn(
        modifier = modifier,
        state = listState
    ) {

        item {
            DetailDescView(bucketDetailUiInfo.descInfo)
        }

        bucketDetailUiInfo.memoInfo?.let {
            item {
                DetailMemoView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 24.dp,
                            start = 28.dp,
                            end = 28.dp
                        ),
                    memo = it.memo
                )
            }
        }

        bucketDetailUiInfo.imageInfo?.let {
            item {
                ImageViewPagerInsideIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 28.dp, end = 28.dp, top = 28.dp, bottom = 100.dp),
                    corner = 8.dp,
                    indicatorModifier = Modifier.padding(bottom = 8.dp, end = 8.dp),
                    imageList = it.imageList
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(84.dp))
        }
    }
}