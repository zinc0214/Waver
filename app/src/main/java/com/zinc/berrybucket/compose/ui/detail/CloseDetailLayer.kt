package com.zinc.berrybucket.compose.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.berrybucket.compose.theme.BaseTheme
import com.zinc.berrybucket.compose.ui.component.ImageViewPagerInsideIndicator
import com.zinc.berrybucket.model.DetailClickEvent
import com.zinc.berrybucket.model.DetailInfo
import com.zinc.berrybucket.model.SuccessButtonInfo

@Composable
fun CloseDetailLayer(
    detailInfo: DetailInfo,
    clickEvent: (DetailClickEvent) -> Unit
) {

    val listScrollState = rememberLazyListState()
    val isScrollable = listScrollState.layoutInfo.visibleItemsInfo.size < totalItemCount(detailInfo)
    val titlePosition = 0

    BaseTheme {
        Scaffold {
            Column(
                modifier = Modifier.fillMaxHeight()
            ) {
                DetailTopAppBar(
                    listState = listScrollState,
                    titlePosition = titlePosition,
                    title = detailInfo.descInfo.title,
                    clickEvent = clickEvent
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
                            .padding(bottom = if (!isScrollable) 0.dp else 28.dp),
                        successClicked = {
                            clickEvent.invoke(DetailClickEvent.SuccessClicked)
                        },
                        successButtonInfo = SuccessButtonInfo(
                            goalCount = detailInfo.descInfo.goalCount,
                            userCount = detailInfo.descInfo.userCount
                        ),
                        isWide = !isScrollable
                    )
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
                        .padding(28.dp)
                        .padding(bottom = 100.dp),
                    corner = 8.dp,
                    indicatorModifier = Modifier.padding(bottom = 8.dp, end = 8.dp),
                    imageList = it.imageList
                )
            }
        }
    }
}

private fun totalItemCount(itemInfo : DetailInfo) : Int{
    var count = 3
    if (itemInfo.memoInfo == null) {
        count -= 1
    }
    if (itemInfo.imageInfo == null) {
        count -= 1
    }
    return count
}