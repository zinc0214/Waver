package com.zinc.berrybucket.compose.ui.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
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
import com.zinc.berrybucket.compose.theme.BaseTheme
import com.zinc.berrybucket.compose.ui.common.ImageViewPagerInsideIndicator
import com.zinc.berrybucket.compose.util.rememberScrollContext
import com.zinc.berrybucket.model.DetailAppBarClickEvent
import com.zinc.berrybucket.model.DetailInfo
import com.zinc.berrybucket.model.SuccessButtonInfo
import com.zinc.berrybucket.presentation.detail.DetailViewModel

@Composable
fun CloseDetailLayer(
    detailId: String,
    backPress: () -> Unit
) {

    val viewModel: DetailViewModel = hiltViewModel()
    viewModel.getBucketDetail(detailId)
    val vmDetailInfo by viewModel.bucketDetailInfo.observeAsState()

    val optionPopUpShowed = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    vmDetailInfo?.let { detailInfo ->
        val listScrollState = rememberLazyListState()
        val scrollContext = rememberScrollContext(listScrollState)
        val titlePosition = 0

        BaseTheme {
            Scaffold { _ ->

                if (optionPopUpShowed.value) {
                    MyDetailAppBarMoreMenuPopupView(optionPopUpShowed)
                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
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
                            detailInfo = detailInfo,
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
    detailInfo: DetailInfo
) {

    LazyColumn(
        modifier = modifier,
        state = listState
    ) {

        item {
            DetailDescLayer(detailInfo.descInfo)
        }

        detailInfo.memoInfo?.let {
            item {
                DetailMemoLayer(
                    modifier = Modifier.padding(
                        top = 24.dp,
                        start = 28.dp,
                        end = 28.dp
                    ),
                    memo = detailInfo.memoInfo.memo
                )
            }
        }

        detailInfo.imageInfo?.let {
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