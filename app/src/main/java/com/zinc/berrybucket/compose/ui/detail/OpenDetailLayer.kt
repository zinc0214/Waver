package com.zinc.berrybucket.compose.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.*
import com.zinc.berrybucket.compose.ui.component.ImageViewPagerInsideIndicator
import com.zinc.berrybucket.compose.ui.component.ProfileView
import com.zinc.berrybucket.model.*

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
    val isScrollable = listScrollState.layoutInfo.visibleItemsInfo.size < totalItemCount(detailInfo)

    if (isScrollEnd != originIsScrollEnd.value) {
        //   isScrollEnded.invoke(isScrollEnd)
        originIsScrollEnd.value = isScrollEnd
    }

    if (!isScrollable) {
        //   isScrollEnded.invoke(true)
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
                        })


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

                    if (originIsScrollEnd.value) {
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CommentEditTextView(
    modifier: Modifier,
    onImeAction: (String) -> Unit
) {
    val hintText = stringResource(id = R.string.commentHintText)
    val keyboardController = LocalSoftwareKeyboardController.current
    var commentText by remember { mutableStateOf(TextFieldValue("")) }

    val likedState = remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Gray3)
    ) {

        IconToggleButton(
            modifier = Modifier
                .padding(top = 11.dp, start = 12.dp, bottom = 11.dp)
                .size(32.dp),
            checked = likedState.value,
            onCheckedChange = {
                likedState.value = !likedState.value
            }) {
            Icon(
                painter = if (likedState.value) painterResource(id = R.drawable.btn_32_like_on)
                else painterResource(id = R.drawable.btn_32_like_off),
                contentDescription = stringResource(id = R.string.likeButtonDesc),
            )
        }

        Row(
            modifier = Modifier
                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp, end = 16.dp)
                .background(color = Gray1, shape = RoundedCornerShape(8.dp))
                .fillMaxWidth()
        ) {
            BasicTextField(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp, start = 10.dp, end = 8.dp),
                value = commentText,
                textStyle = TextStyle(
                    color = Gray9,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                ),
                onValueChange = { commentText = it },
                maxLines = 3,
                keyboardActions = KeyboardActions(onPrevious = {
                    onImeAction(commentText.text)
                    keyboardController?.hide()
                }),
                decorationBox = { innerTextField ->
                    if (commentText.text.isBlank()) {
                        Text(text = hintText, color = Gray6, fontSize = 14.sp)
                    }
                    Row {
                        innerTextField()  //<-- Add this
                    }
                },
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                modifier = Modifier
                    .padding(4.dp)
                    .then(Modifier.size(28.dp))
                    .align(Alignment.Bottom),
                onClick = { /*TODO*/ }) {
                Icon(
                    painter =
                    if (commentText.text.isBlank())
                        painterResource(id = R.drawable.comment_send_off)
                    else painterResource(
                        id = R.drawable.comment_send_on
                    ),
                    contentDescription = stringResource(id = R.string.commentSendButtonDesc)
                )
            }
        }

    }

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