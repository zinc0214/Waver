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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.berrybucket.model.CommentLongClicked
import com.zinc.berrybucket.model.CommentTagInfo
import com.zinc.berrybucket.model.DetailAppBarClickEvent
import com.zinc.berrybucket.model.DetailClickEvent
import com.zinc.berrybucket.model.DetailInfo
import com.zinc.berrybucket.model.ProfileInfo
import com.zinc.berrybucket.model.SuccessButtonInfo
import com.zinc.berrybucket.ui.design.theme.BaseTheme
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.util.Keyboard
import com.zinc.berrybucket.ui.design.util.keyboardAsState
import com.zinc.berrybucket.ui.design.util.visibleLastIndex
import com.zinc.berrybucket.ui.presentation.GoToBucketDetailEvent
import com.zinc.berrybucket.ui.presentation.common.ImageViewPagerInsideIndicator
import com.zinc.berrybucket.ui.presentation.common.ProfileView
import com.zinc.berrybucket.ui.presentation.detail.DetailViewModel
import com.zinc.berrybucket.ui.presentation.detail.component.CommentCountView
import com.zinc.berrybucket.ui.presentation.detail.component.CommentLine
import com.zinc.berrybucket.ui.presentation.detail.component.CommentOptionClicked
import com.zinc.berrybucket.ui.presentation.detail.component.CommentSelectedDialog
import com.zinc.berrybucket.ui.presentation.detail.component.DetailCommentView
import com.zinc.berrybucket.ui.presentation.detail.component.DetailCommenterTagDropDownView
import com.zinc.berrybucket.ui.presentation.detail.component.DetailDescView
import com.zinc.berrybucket.ui.presentation.detail.component.DetailMemoView
import com.zinc.berrybucket.ui.presentation.detail.component.DetailSuccessButtonView
import com.zinc.berrybucket.ui.presentation.detail.component.DetailTopAppBar
import com.zinc.berrybucket.ui.presentation.detail.component.MyDetailAppBarMoreMenuDialog
import com.zinc.berrybucket.ui.presentation.detail.component.TaggableEditText
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.common.models.ReportInfo

@Composable
fun OpenDetailLayer(
    detailId: String,
    goToEvent: (GoToBucketDetailEvent) -> Unit,
    backPress: () -> Unit
) {

    val viewModel: DetailViewModel = hiltViewModel()
    viewModel.getBucketDetail("open") // TODO : 실제 DetailId 를 보는 것으로 수정 필요
    viewModel.getCommentTaggableList()

    val vmDetailInfo by viewModel.bucketDetailInfo.observeAsState()
    val originCommentTaggableList by viewModel.originCommentTaggableList.observeAsState()

    val validTaggableList = remember {
        mutableListOf<CommentTagInfo>()
    }

    vmDetailInfo?.let { detailInfo ->

        // scroll 상태에 따른 버튼 상태
        val listScrollState = rememberLazyListState()
        val titleIndex = if (detailInfo.imageInfo == null) 1 else 2 // 타이틀의 위치
        val flatButtonIndex = flatButtonIndex(detailInfo) // 붙는 버튼의 위치
        val visibleLastIndex = listScrollState.visibleLastIndex() // 현재 보여지는 마지막 아이템의 index
        val isCommentViewShown =
            visibleLastIndex > listScrollState.layoutInfo.totalItemsCount - 2 // 댓글이 보이는지 여부 ( -2 == 댓글이 마지막이고, 그 전의 카운터에서부터 노출하기 위해)
        val isScrollable =
            if (visibleLastIndex == 0) true else visibleLastIndex <= listScrollState.layoutInfo.totalItemsCount // 미자믹 아이템  == 전체아이템 갯수 인지 확인

        // 팝업 노출 여부
        val optionPopUpShowed = remember { mutableStateOf(false) } // 우상단 옵션 팝업 노출 여부
        val commentOptionPopUpShowed =
            remember { mutableStateOf(false to 0) } // 댓글 롱클릭 옵션 팝업 노출여부 + 댓글 index

        // 키보드 상태 확인
        val isKeyBoardOpened = remember { mutableStateOf(true) } // 키보드 오픈 상태 확인
        val isKeyboardStatus by keyboardAsState()
        val focusManager = LocalFocusManager.current
        if (isKeyboardStatus == Keyboard.Closed) {
            focusManager.clearFocus()
            isKeyBoardOpened.value = false
        } else {
            isKeyBoardOpened.value = true
        }

        // 전체 뷰 clickable 인데, 리플 효과 제거를 위해 사용
        val interactionSource = remember { MutableInteractionSource() }

        // ComposeView 에서 observer 사용을 위해
        val lifecycleOwner = LocalLifecycleOwner.current

        BaseTheme {
            Scaffold { _ ->

                if (optionPopUpShowed.value) {
                    MyDetailAppBarMoreMenuDialog(optionPopUpShowed)
                }

                if (commentOptionPopUpShowed.value.first) {
                    val commenter = vmDetailInfo?.commentInfo?.commenterList?.getOrNull(
                        commentOptionPopUpShowed.value.second
                    )

                    if (commenter != null) {
                        CommentSelectedDialog(
                            commenter = commenter,
                            onDismissRequest = {
                                commentOptionPopUpShowed.value =
                                    false to commentOptionPopUpShowed.value.second
                            },
                            commentOptionClicked = {
                                when (it) {
                                    is CommentOptionClicked.FirstOptionClicked -> TODO()
                                    is CommentOptionClicked.SecondOptionClicked -> {
                                        if (commenter.isMine) {
                                            // go to ..?
                                        } else {
                                            val reportInfo = ReportInfo(
                                                id = commenter.commentId,
                                                writer = commenter.nickName,
                                                contents = commenter.comment
                                            )

                                            // 신고이벤트
                                            goToEvent.invoke(
                                                GoToBucketDetailEvent.GoToCommentReport(reportInfo)
                                            )
                                        }
                                    }
                                }
                            }
                        )
                    }
                }


                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .clickable(interactionSource = interactionSource,
                            indication = null,
                            onClick = { optionPopUpShowed.value = false })
                ) {
                    DetailTopAppBar(
                        listState = listScrollState,
                        titlePosition = titleIndex,
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
                        })

                    ConstraintLayout(
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        val (contentView, floatingButtonView, editView) = createRefs()

                        ContentView(listState = listScrollState,
                            detailInfo = detailInfo,
                            clickEvent = {
                                when (it) {
                                    is CommentLongClicked -> {
                                        commentOptionPopUpShowed.value = true to it.commentIndex
                                    }
                                    DetailClickEvent.SuccessClicked -> TODO()
                                }
                            },
                            flatButtonIndex = flatButtonIndex,
                            isCommentViewShown = isCommentViewShown,
                            modifier = Modifier
                                .constrainAs(contentView) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    height = Dimension.fillToConstraints
                                })

                        if (listScrollState.layoutInfo.visibleItemsInfo.isNotEmpty()) {
                            // 플로팅 완료 버튼
                            if ((listScrollState.layoutInfo.visibleItemsInfo.last().key.hashCode() < flatButtonIndex)) {
                                DetailSuccessButtonView(modifier = Modifier
                                    .constrainAs(
                                        floatingButtonView
                                    ) {
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                        if (isCommentViewShown) {
                                            bottom.linkTo(editView.top)
                                        } else {
                                            bottom.linkTo(parent.bottom)
                                        }
                                    }
                                    .padding(bottom = 30.dp), successClicked = {
                                    // TODO : viewModel Scuccess
                                }, successButtonInfo = SuccessButtonInfo(
                                    goalCount = detailInfo.descInfo.goalCount,
                                    userCount = detailInfo.descInfo.userCount
                                )
                                )
                            }
                        }


                        // 최하단 EditTextView
                        if (isCommentViewShown || !isScrollable) {
                            AndroidView(modifier = Modifier
                                .constrainAs(editView) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                }
                                .height(if (isKeyBoardOpened.value) 52.dp else 68.dp)
                                .fillMaxWidth(), factory = {
                                TaggableEditText(it).apply {
                                    setUpView(viewModel = viewModel,
                                        lifecycleOwner = lifecycleOwner,
                                        originCommentTaggableList = originCommentTaggableList
                                            ?: emptyList(),
                                        updateValidTaggableList = { validList ->
                                            validTaggableList.clear()
                                            validTaggableList.addAll(validList)
                                        },
                                        commentSendButtonClicked = {
                                            focusManager.clearFocus()
                                        })
                                }
                            })
                        }
                    }
                }

                // 유효한 태그가 있을 때만 노출
                if (validTaggableList.isNotEmpty() && isKeyBoardOpened.value) {
                    DetailCommenterTagDropDownView(
                        commentTaggableList = validTaggableList,
                        tagClicked = {
                            viewModel.taggedClicked(it)
                        })
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
    isCommentViewShown: Boolean,
    clickEvent: (DetailClickEvent) -> Unit
) {

    LazyColumn(
        modifier = modifier, state = listState
    ) {
        detailInfo.imageInfo?.let {
            item(key = "imageViewPager") {
                ImageViewPagerInsideIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    indicatorModifier = Modifier.padding(bottom = 16.dp, end = 16.dp),
                    imageList = it.imageList
                )
            }
        }

        item(key = "profileView") {
            ProfileView(detailInfo.profileInfo)
        }

        item(key = "detailDescLayer") {
            DetailDescView(detailInfo.descInfo)
        }

        item(key = "memoView") {
            if (detailInfo.memoInfo != null) {
                DetailMemoView(
                    modifier = Modifier.padding(
                        top = 24.dp, start = 28.dp, end = 28.dp
                    ), memo = detailInfo.memoInfo!!.memo
                )
            } else {
                Spacer(modifier = Modifier.height(56.dp))
            }

        }

        // TODO : 인터렉션 이슈 해결 필요

        item(key = "flatDetailSuccessButton") {
            if (listState.layoutInfo.visibleItemsInfo.isEmpty()) {
                return@item
            }
            if (listState.layoutInfo.visibleItemsInfo.last().key.hashCode() >= flatButtonIndex || isCommentViewShown) {
                DetailSuccessButtonView(
                    successClicked = {
                        clickEvent.invoke(DetailClickEvent.SuccessClicked)
                    }, successButtonInfo = SuccessButtonInfo(
                        goalCount = detailInfo.descInfo.goalCount,
                        userCount = detailInfo.descInfo.userCount
                    ),
                    modifier = Modifier.padding(top = 30.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }

        item(key = "spacer") {
            Spacer(modifier = Modifier.height(30.dp))
        }


        detailInfo.commentInfo?.let {
            item(key = "commentLine") {
                CommentLine()
            }
            item(key = "commentCountView") {
                CommentCountView(it.commentCount)
            }
            item(key = "commentLayer") {
                DetailCommentView(commentInfo = it, commentLongClicked = {
                    clickEvent.invoke(CommentLongClicked(it))
                })
            }
        }
    }
}

@Composable
fun ProfileView(profileInfo: ProfileInfo) {
    ProfileView(
        modifier = Modifier.padding(top = 28.dp),
        imageSize = 48.dp,
        profileSize = 44.dp,
        profileRadius = 16.dp,
        badgeSize = Pair(24.dp, 27.dp),
        nickNameTextSize = dpToSp(14.dp),
        titlePositionTextSize = dpToSp(13.dp),
        nickNameTextColor = Gray10,
        profileInfo = profileInfo.toUi()
    )
}

private fun flatButtonIndex(detailInfo: DetailInfo): Int {
    var index = 6
    if (detailInfo.imageInfo == null) {
        index -= 1
    }
    if (detailInfo.memoInfo == null) {
        index -= 1
    }
    return index
}