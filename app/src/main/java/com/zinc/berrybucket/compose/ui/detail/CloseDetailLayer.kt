package com.zinc.berrybucket.compose.ui.detail

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.BaseTheme
import com.zinc.berrybucket.compose.theme.Gray1
import com.zinc.berrybucket.compose.theme.Gray10
import com.zinc.berrybucket.compose.ui.common.ImageViewPagerInsideIndicator
import com.zinc.berrybucket.compose.util.rememberScrollContext
import com.zinc.berrybucket.model.DetailAppBarClickEvent
import com.zinc.berrybucket.model.DetailClickEvent
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
                    MoreMenuPopupView(optionPopUpShowed)
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

@Composable
private fun MoreMenuPopupView(optionPopUpShowed: MutableState<Boolean>) {

    Card(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopEnd)
            .padding(top = 8.dp, bottom = 8.dp),
        shape = RoundedCornerShape(40.dp),
        backgroundColor = Gray1,
        elevation = 3.dp
    ) {
        DropdownMenu(
            expanded = true,
            onDismissRequest = { optionPopUpShowed.value = false },
            offset = DpOffset(16.dp, 0.dp),
            properties = PopupProperties(clippingEnabled = false)
        ) {
            DropdownMenuItem(
                onClick = {
                    // TODO : Go To Edit
                },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .sizeIn(
                        maxHeight = 36.dp
                    )
            ) {
                PppUpText(R.string.edit)
            }
            DropdownMenuItem(
                onClick = {
                    // TODO : Go To Count Change
                },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .sizeIn(
                        maxHeight = 36.dp
                    )
            ) {
                PppUpText(R.string.countChange)
            }
            DropdownMenuItem(
                onClick = {
                    // TODO : Go To Delete
                },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .sizeIn(
                        maxHeight = 36.dp
                    )
            ) {
                PppUpText(R.string.delete)
            }
        }
    }
}

@Composable
private fun PppUpText(@StringRes text: Int) {
    Text(
        text = stringResource(id = text),
        color = Gray10,
        fontSize = 14.sp,
        modifier = Modifier.padding(start = 16.dp, end = 24.dp)
    )
}