package com.zinc.berrybucket.ui.presentation.detail.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.berrybucket.model.BucketDetailUiInfo
import com.zinc.berrybucket.model.DetailAppBarClickEvent
import com.zinc.berrybucket.model.SuccessButtonInfo
import com.zinc.berrybucket.ui.design.theme.BaseTheme
import com.zinc.berrybucket.ui.design.util.rememberScrollContext
import com.zinc.berrybucket.ui.presentation.component.ImageViewPagerInsideIndicator
import com.zinc.berrybucket.ui.presentation.detail.DetailViewModel
import com.zinc.berrybucket.ui.presentation.detail.component.DetailDescView
import com.zinc.berrybucket.ui.presentation.detail.component.DetailMemoView
import com.zinc.berrybucket.ui.presentation.detail.component.DetailSuccessButtonView
import com.zinc.berrybucket.ui.presentation.detail.component.DetailTopAppBar
import com.zinc.berrybucket.ui.presentation.detail.component.GoalCountUpdateDialog
import com.zinc.berrybucket.ui.presentation.detail.component.MyDetailAppBarMoreMenuDialog
import com.zinc.berrybucket.ui.presentation.detail.model.GoalCountUpdateEvent

@Composable
fun CloseDetailLayer(
    detailId: String,
    backPress: () -> Unit
) {

    val viewModel: DetailViewModel = hiltViewModel()

    val vmDetailInfo by viewModel.bucketBucketDetailUiInfo.observeAsState()

    if (vmDetailInfo == null) {
        viewModel.getBucketDetail(detailId)
    }

    val optionPopUpShowed = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val goalCountUpdatePopUpShowed = remember { mutableStateOf(false) } // 달성횟수 팝업 노출 여부

    vmDetailInfo?.let { detailInfo ->
        val listScrollState = rememberLazyListState()
        val scrollContext = rememberScrollContext(listScrollState)
        val titlePosition = 0

        BaseTheme {
            Scaffold { padding ->

                if (optionPopUpShowed.value) {
                    MyDetailAppBarMoreMenuDialog(optionPopUpShowed) {
                        when (it) {
                            MyBucketMenuEvent.GoToDelete -> {
                                optionPopUpShowed.value = false
                            }

                            MyBucketMenuEvent.GoToEdit -> {
                                optionPopUpShowed.value = false
                            }

                            MyBucketMenuEvent.GoToGoalUpdate -> {
                                goalCountUpdatePopUpShowed.value = true
                                optionPopUpShowed.value = false
                            }
                        }
                    }
                }

                if (goalCountUpdatePopUpShowed.value) {
                    GoalCountUpdateDialog(
                        currentCount = detailInfo.descInfo.goalCount.toString()
                    ) {
                        when (it) {
                            GoalCountUpdateEvent.Close -> {
                                goalCountUpdatePopUpShowed.value = false
                            }

                            is GoalCountUpdateEvent.CountUpdate -> {
                                // Todo : ViewModel Update!
                                viewModel.goalCountUpdate(detailInfo.bucketId, it.count)
                                goalCountUpdatePopUpShowed.value = false
                            }
                        }
                    }
                }


                Column(
                    modifier = Modifier
                        .fillMaxHeight()
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
                        title = detailInfo.descInfo.title,
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
                            bucketDetailUiInfo = detailInfo,
                            modifier = Modifier.constrainAs(contentView) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)
                                height = Dimension.fillToConstraints
                            })

                        DetailSuccessButtonView(
                            modifier = Modifier
                                .constrainAs(floatingButtonView) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                }
                                .padding(bottom = if (scrollContext.isBottom) 28.dp else 0.dp),
                            successClicked = {
                                //clickEvent.invoke(DetailClickEvent.SuccessClicked)
                            },
                            successButtonInfo = SuccessButtonInfo(
                                goalCount = detailInfo.descInfo.goalCount,
                                userCount = detailInfo.descInfo.userCount
                            ),
                            isWide = !scrollContext.isBottom
                        )
                    }
                }
            }
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
                    modifier = Modifier.padding(
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
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}