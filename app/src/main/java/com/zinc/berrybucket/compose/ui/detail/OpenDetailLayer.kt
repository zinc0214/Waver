package com.zinc.berrybucket.compose.ui.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.BaseTheme
import com.zinc.berrybucket.compose.theme.Gray10
import com.zinc.berrybucket.compose.theme.Gray7
import com.zinc.berrybucket.compose.ui.component.ImageViewPagerInsideIndicator
import com.zinc.berrybucket.compose.ui.component.ProfileView
import com.zinc.berrybucket.model.DetailInfo
import com.zinc.berrybucket.model.ProfileInfo

@Composable
fun OpenDetailLayer(
    detailInfo: DetailInfo,
    clickEvent: (DetailClickEvent) -> Unit,
    isScrollEnded: (Boolean) -> Unit
) {

    val listScrollState = rememberLazyListState()
    val titlePosition = if (detailInfo.imageInfo == null) 1 else 2
    val flatButtonIndex = flatButtonIndex(detailInfo)
    val isScrollEnd = listScrollState.firstVisibleItemIndex >= flatButtonIndex - 2
    val originIsScrollEnd = remember { mutableStateOf(false) }

    if (isScrollEnd != originIsScrollEnd.value) {
        isScrollEnded.invoke(isScrollEnd)
        originIsScrollEnd.value = isScrollEnd
    }

    BaseTheme {
        Scaffold {
            Column {

                DetailTopAppBar(
                    listState = listScrollState,
                    titlePosition = titlePosition,
                    title = detailInfo.descInfo.title,
                    clickEvent = clickEvent
                )

                ConstraintLayout {
                    val (contentView, floatingButtonView) = createRefs()

                    ContentView(
                        listState = listScrollState,
                        detailInfo = detailInfo,
                        clickEvent = clickEvent,
                        flatButtonIndex = flatButtonIndex,
                        modifier = Modifier.constrainAs(contentView) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                        })

                    if ((listScrollState.visibleLastIndex() < flatButtonIndex) && !isScrollEnd) {
                        DetailSuccessButtonView(
                            modifier = Modifier
                                .constrainAs(floatingButtonView) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                }
                                .padding(bottom = 30.dp),
                            successClicked = {
                                clickEvent.invoke(DetailClickEvent.SuccessClicked)
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun DetailTopAppBar(
    listState: LazyListState,
    titlePosition: Int,
    title: String,
    clickEvent: (DetailClickEvent) -> Unit
) {

    val isTitleScrolled = listState.firstVisibleItemIndex > titlePosition

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        val (closeButton, titleView, moreButton) = createRefs()

        Icon(
            painter = painterResource(id = R.drawable.btn40close),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 14.dp, top = 6.dp, bottom = 6.dp)
                .clickable {
                    clickEvent(DetailClickEvent.CloseClicked)
                }
                .constrainAs(closeButton) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )

        if (isTitleScrolled) {
            Text(
                color = Gray10,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                text = title,
                modifier = Modifier
                    .padding(top = 14.dp, bottom = 14.dp)
                    .constrainAs(titleView) {
                        start.linkTo(closeButton.end)
                        end.linkTo(moreButton.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.btn32more),
            contentDescription = null,
            modifier = Modifier
                .padding(end = 14.dp, top = 6.dp, bottom = 6.dp)
                .clickable {
                    clickEvent(DetailClickEvent.MoreOptionClicked)
                }
                .constrainAs(moreButton) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}

@Composable
private fun ContentView(
    modifier: Modifier,
    listState: LazyListState,
    detailInfo: DetailInfo,
    flatButtonIndex: Int,
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

        detailInfo.memoInfo?.let {
            item {
                MemoView(
                    modifier = Modifier.padding(
                        top = 24.dp,
                        start = 28.dp,
                        end = 28.dp,
                        bottom = 56.dp
                    ),
                    memo = detailInfo.memoInfo.memo
                )
            }
        }

        // TODO : 인터렉션 이슈 해결 필요
        item {
            if (listState.visibleLastIndex() > flatButtonIndex) {
                DetailSuccessButtonView(
                    successClicked = {
                        clickEvent.invoke(DetailClickEvent.SuccessClicked)
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }

        item {
            DetailCommentLayer(commentInfo = detailInfo.commentInfo,
                commentLongClicked = {
                    clickEvent.invoke(DetailClickEvent.CommentLongClicked(it))
                }
            )
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

@Composable
private fun MemoView(modifier: Modifier = Modifier, memo: String) {
    Text(
        text = memo,
        color = Gray7,
        fontSize = 15.sp,
        modifier = modifier
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

private fun floatingButtonVisibleIndex(detailInfo: DetailInfo) = flatButtonIndex(detailInfo) - 1


sealed class DetailClickEvent {
    object MoreOptionClicked : DetailClickEvent()
    object CloseClicked : DetailClickEvent()
    object SuccessClicked : DetailClickEvent()
    data class CommentLongClicked(val commentId: String) : DetailClickEvent()
}

fun LazyListState.isScrolledToTheEnd() =
    visibleLastIndex() == layoutInfo.viewportEndOffset

fun LazyListState.visibleLastIndex() = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
