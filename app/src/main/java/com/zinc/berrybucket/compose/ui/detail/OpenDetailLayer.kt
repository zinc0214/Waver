package com.zinc.berrybucket.compose.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.berrybucket.compose.theme.BaseTheme
import com.zinc.berrybucket.compose.theme.Gray10
import com.zinc.berrybucket.compose.ui.common.ImageViewPagerInsideIndicator
import com.zinc.berrybucket.compose.ui.common.ProfileView
import com.zinc.berrybucket.model.*

@Composable
fun OpenDetailLayer(
    detailInfo: DetailInfo,
    clickEvent: (DetailClickEvent) -> Unit,
) {

    val listScrollState = rememberLazyListState()
    val titlePosition = if (detailInfo.imageInfo == null) 1 else 2
    val flatButtonIndex = flatButtonIndex(detailInfo)
    val isScrollEnd = listScrollState.firstVisibleItemIndex >= flatButtonIndex - 2
    val originIsScrollEnd = remember { mutableStateOf(false) }
    val isScrollable = listScrollState.layoutInfo.visibleItemsInfo.size < totalItemCount(detailInfo)

    if (isScrollEnd != originIsScrollEnd.value) {
        originIsScrollEnd.value = isScrollEnd
    }

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
                    val (contentView, floatingButtonView, editView) = createRefs()

                    ContentView(
                        listState = listScrollState,
                        detailInfo = detailInfo,
                        clickEvent = clickEvent,
                        flatButtonIndex = flatButtonIndex,
                        isScrollable = isScrollable,
                        modifier = Modifier.constrainAs(contentView) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            if (originIsScrollEnd.value) {
                                bottom.linkTo(editView.top)
                            } else {
                                bottom.linkTo(parent.bottom)
                            }
                        }.fillMaxHeight())


                    if ((listScrollState.visibleLastIndex() < flatButtonIndex) && !isScrollEnd) {
                        DetailSuccessButtonView(
                            modifier = Modifier
                                .constrainAs(floatingButtonView) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    if (originIsScrollEnd.value) {
                                        bottom.linkTo(editView.top)
                                    } else {
                                        bottom.linkTo(parent.bottom)
                                    }
                                }
                                .padding(bottom = 30.dp),
                            successClicked = {
                                clickEvent.invoke(DetailClickEvent.SuccessClicked)
                            },
                            successButtonInfo = SuccessButtonInfo(
                                goalCount = detailInfo.descInfo.goalCount,
                                userCount = detailInfo.descInfo.userCount
                            )
                        )
                    }

                    if (originIsScrollEnd.value || !isScrollable) {
                        CommentEditTextView(
                            modifier = Modifier
                                .constrainAs(editView) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                },
                            onImeAction = {})
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
    detailInfo: DetailInfo,
    flatButtonIndex: Int,
    isScrollable: Boolean,
    clickEvent: (DetailClickEvent) -> Unit
) {

    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        detailInfo.imageInfo?.let {
            item {
                ImageViewPagerInsideIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    indicatorModifier = Modifier.padding(bottom = 16.dp, end = 16.dp),
                    imageList = it.imageList
                )
            }
        }

        item {
            ProfileView(detailInfo.profileInfo)
        }

        item {
            DetailDescLayer(detailInfo.descInfo)
        }

        item {
            if (detailInfo.memoInfo != null) {
                DetailMemoLayer(
                    modifier = Modifier.padding(
                        top = 24.dp,
                        start = 28.dp,
                        end = 28.dp,
                        bottom = 56.dp
                    ),
                    memo = detailInfo.memoInfo.memo
                )
            } else {
                Spacer(modifier = Modifier.height(56.dp))
            }

        }

        // TODO : 인터렉션 이슈 해결 필요
        item {
            if (listState.visibleLastIndex() > flatButtonIndex || !isScrollable) {
                DetailSuccessButtonView(
                    successClicked = {
                        clickEvent.invoke(DetailClickEvent.SuccessClicked)
                    },
                    successButtonInfo = SuccessButtonInfo(
                        goalCount = detailInfo.descInfo.goalCount,
                        userCount = detailInfo.descInfo.userCount
                    )
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }

        detailInfo.commentInfo?.let {
            item {
                DetailCommentLayer(commentInfo = detailInfo.commentInfo,
                    commentLongClicked = {
                        clickEvent.invoke(CommentLongClicked(it))
                    }
                )
            }
        }
    }
}

@Composable
private fun ProfileView(profileInfo: ProfileInfo) {
    ProfileView(
        modifier = Modifier.padding(top = 28.dp),
        imageSize = 48.dp,
        profileSize = 44.dp,
        profileRadius = 16.dp,
        badgeSize = Pair(24.dp, 27.dp),
        nickNameTextSize = 14.sp,
        titlePositionTextSize = 13.sp,
        nickNameTextColor = Gray10,
        profileInfo = profileInfo
    )
}

private fun flatButtonIndex(detailInfo: DetailInfo): Int {
    var index = 5
    if (detailInfo.imageInfo == null) {
        index -= 1
    }
    if (detailInfo.memoInfo == null) {
        index -= 1
    }
    return index
}

private fun LazyListState.visibleLastIndex() = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

private fun totalItemCount(detailInfo: DetailInfo): Int {
    var count = 7
    if (detailInfo.imageInfo == null) {
        count -= 1
    }
    if (detailInfo.memoInfo == null) {
        count -= 1
    }
    if (detailInfo.commentInfo == null) {
        count -= 1
    }
    return count
}