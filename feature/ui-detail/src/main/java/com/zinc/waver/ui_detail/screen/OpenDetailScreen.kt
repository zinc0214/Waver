package com.zinc.waver.ui_detail.screen

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.common.models.AddBucketCommentRequest
import com.zinc.common.models.BucketStatus
import com.zinc.common.models.YesOrNo
import com.zinc.waver.model.BucketDetailUiInfo
import com.zinc.waver.model.Comment
import com.zinc.waver.model.CommentLongClicked
import com.zinc.waver.model.CommentMentionInfo
import com.zinc.waver.model.DetailAppBarClickEvent
import com.zinc.waver.model.DetailClickEvent
import com.zinc.waver.model.DetailDescType
import com.zinc.waver.model.DetailLoadFailStatus
import com.zinc.waver.model.ReportInfo
import com.zinc.waver.model.ReportType
import com.zinc.waver.model.SuccessButtonInfo
import com.zinc.waver.model.WriteCategoryInfo
import com.zinc.waver.model.WriteOpenType
import com.zinc.waver.ui.design.util.Keyboard
import com.zinc.waver.ui.design.util.isLastItemVisible
import com.zinc.waver.ui.design.util.keyboardAsState
import com.zinc.waver.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.waver.ui_common.R
import com.zinc.waver.ui_detail.component.DetailSuccessButtonView
import com.zinc.waver.ui_detail.component.DetailTopAppBar
import com.zinc.waver.ui_detail.component.GoalCountUpdateDialog
import com.zinc.waver.ui_detail.component.MyCommentOptionClicked
import com.zinc.waver.ui_detail.component.MyCommentSelectedDialog
import com.zinc.waver.ui_detail.component.MyDetailAppBarMoreMenuDialog
import com.zinc.waver.ui_detail.component.OpenDetailContentView
import com.zinc.waver.ui_detail.component.OtherCommentOptionClicked
import com.zinc.waver.ui_detail.component.OtherCommentSelectedDialog
import com.zinc.waver.ui_detail.component.OtherDetailAppBarMoreMenuDialog
import com.zinc.waver.ui_detail.component.ShowReportScreen
import com.zinc.waver.ui_detail.component.mention.CommentEditTextView2
import com.zinc.waver.ui_detail.component.mention.MentionSearchListPopup
import com.zinc.waver.ui_detail.model.GoalCountUpdateEvent
import com.zinc.waver.ui_detail.model.MentionSearchInfo
import com.zinc.waver.ui_detail.model.MyBucketMoreMenuEvent
import com.zinc.waver.ui_detail.model.OpenBucketDetailEvent2
import com.zinc.waver.ui_detail.model.OpenBucketDetailInternalEvent
import com.zinc.waver.ui_detail.model.OpenDetailEditTextViewEvent
import com.zinc.waver.ui_detail.model.OtherBucketMenuEvent
import com.zinc.waver.ui_detail.model.TaggedTextInfo
import com.zinc.waver.ui_detail.model.toUpdateUiModel
import com.zinc.waver.ui_detail.viewmodel.DetailViewModel
import com.zinc.waver.util.createImageInfoWithPath
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalTime

@Composable
fun OpenDetailScreen(
    detailId: String,
    writerId: String,
    isMine: Boolean,
    goToEvent: (OpenBucketDetailEvent2) -> Unit,
    backPress: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val detailInfoAsState by viewModel.bucketBucketDetailUiInfo.observeAsState()
    val validMentionList by viewModel.validMentionList.observeAsState()
    val loadFailAsState by viewModel.loadFail.observeAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    var detailInfo by remember { mutableStateOf(detailInfoAsState) }
    var internalEvent: OpenBucketDetailInternalEvent by remember {
        mutableStateOf(
            OpenBucketDetailInternalEvent.None
        )
    }

    LaunchedEffect(Unit) {
        viewModel.loadInitData(detailId, writerId, isMine)
    }

    LaunchedEffect(loadFailAsState) {
        loadFailAsState?.let { internalEvent = OpenBucketDetailInternalEvent.Error(it) }
    }

    LaunchedEffect(detailInfoAsState) {
        detailInfoAsState?.let {
            detailInfo = it
        }
    }

    LaunchedEffect(internalEvent) {
        val event = internalEvent

        if (event is OpenBucketDetailInternalEvent.ViewModelEvent) {
            when (event) {
                OpenBucketDetailInternalEvent.ViewModelEvent.Achieve -> {
                    viewModel.achieveMyBucket()
                }

                is OpenBucketDetailInternalEvent.ViewModelEvent.DeleteMyComment -> {
                    viewModel.requestDeleteBucketComment(event.commentId)
                }

                OpenBucketDetailInternalEvent.ViewModelEvent.UpdateBucket -> {
                    viewModel.getBucketDetail(
                        bucketId = detailId,
                        writerId = writerId,
                        isMine = isMine
                    )
                }

                is OpenBucketDetailInternalEvent.ViewModelEvent.UpdateGoalCount -> {
                    viewModel.requestGoalCountUpdate(event.count)
                }

                is OpenBucketDetailInternalEvent.ViewModelEvent.AddComment -> {
                    viewModel.requestAddBucketComment(event.comment)
                }

                OpenBucketDetailInternalEvent.ViewModelEvent.BucketLike -> {
                    viewModel.saveBucketLike()
                }
            }
        }
    }

    detailInfo?.let { info ->
        InternalOpenDetailScreen(
            detailInfo = info,
            internalEvent = internalEvent,
            validMentionList = validMentionList.orEmpty(),
            goToOutEvent = goToEvent,
            updateInternalEvent = { event ->
                internalEvent = event
            },
            backPress = {
                keyboardController?.hide()
                backPress()
            }
        )
    }
}

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
private fun InternalOpenDetailScreen(
    detailInfo: BucketDetailUiInfo,
    validMentionList: List<CommentMentionInfo>,
    internalEvent: OpenBucketDetailInternalEvent,
    goToOutEvent: (OpenBucketDetailEvent2) -> Unit,
    updateInternalEvent: (OpenBucketDetailInternalEvent) -> Unit,
    backPress: () -> Unit,
) {

    val context = LocalContext.current
    val keyboardStatus by keyboardAsState()

    val listScrollState = rememberLazyListState()
    val titleIndex = if (detailInfo.imageInfo == null) 1 else 2

    // 버킷리스트 content 내부에 달성완료 버튼을 노출할 여부
    val needToAddButtonSpace = detailInfo.canShowCompleteButton

    // 하단 comment 영역 노출 가능 여부
    var isAvailableShowComment by remember { mutableStateOf(listScrollState.isLastItemVisible) }

    var memoHeight by remember { mutableIntStateOf(0) }

    // 댓글 값 저장
    val commentText = remember { mutableStateOf("") }

    // 새로 선택된 태그 정보
    val newTaggedText: MutableState<TaggedTextInfo?> = remember { mutableStateOf(null) }

    // 태그된 사람들
    val taggedMemberList = mutableListOf<String>()

    // 검색할 텍스트와 관련된 정보들
    val mentionSearchInfo: MutableState<MentionSearchInfo?> = remember { mutableStateOf(null) }

    LaunchedEffect(listScrollState.isLastItemVisible) {
        if (memoHeight == 0 && needToAddButtonSpace) {
            val targetItem2 = listScrollState.layoutInfo.visibleItemsInfo
                .find { it.contentType == "buttonSpace" }

            memoHeight = targetItem2?.offset ?: 0
        }
    }

    LaunchedEffect(listScrollState) {
        snapshotFlow { listScrollState.firstVisibleItemScrollOffset }
            .distinctUntilChanged()
            .onEach {
                val targetItem = listScrollState.layoutInfo.visibleItemsInfo
                    .find { it.contentType == "buttonSpace" }

                memoHeight = targetItem?.offset ?: 0

                val isBottom = !listScrollState.canScrollForward
                val isShowComment = listScrollState.isLastItemVisible

                isAvailableShowComment =
                    isBottom || isShowComment || keyboardStatus == Keyboard.Opened
            }
            .launchIn(this)
    }

    LaunchedEffect(keyboardStatus) {
        if (keyboardStatus == Keyboard.Opened) {
            val lastIndex = listScrollState.layoutInfo.totalItemsCount - 1
            listScrollState.animateScrollToItem(lastIndex)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .statusBarsPadding()
            .imePadding()
            .navigationBarsPadding()
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
                        updateInternalEvent(OpenBucketDetailInternalEvent.MoreDialog(detailInfo.isMine))
                    }
                }
            })

        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (contentView, floatingButtonView, editView) = createRefs()

            OpenDetailContentView(
                listState = listScrollState,
                info = detailInfo,
                clickEvent = {
                    when (it) {
                        is CommentLongClicked -> {
                            updateInternalEvent(OpenBucketDetailInternalEvent.CommentOption(it.commentIndex))
                        }

                        is DetailClickEvent.SuccessClicked -> {
                            updateInternalEvent(OpenBucketDetailInternalEvent.ViewModelEvent.Achieve)
                        }

                        is DetailClickEvent.GoToOtherProfile -> {
                            goToOutEvent.invoke(OpenBucketDetailEvent2.GoToOtherProfile(it.id))
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(contentView) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    })

            if (detailInfo.canShowCompleteButton && keyboardStatus == Keyboard.Closed) {
                DetailSuccessButtonView(
                    modifier = Modifier
                        .padding(bottom = 30.dp)
                        .offset {
                            IntOffset(
                                x = 0,
                                y = memoHeight
                            )
                        }
                        .constrainAs(
                            floatingButtonView
                        ) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            if (!needToAddButtonSpace || memoHeight == 0) {
                                bottom.linkTo(parent.bottom)
                            } else {
                                top.linkTo(parent.top)
                            }
                        },
                    successClicked = {
                        updateInternalEvent(OpenBucketDetailInternalEvent.ViewModelEvent.Achieve)
                    },
                    successButtonInfo = SuccessButtonInfo(
                        goalCount = detailInfo.descInfo.goalCount,
                        userCount = detailInfo.descInfo.userCount
                    )
                )
            }

            // 최하단 EditTextView
            Column(modifier = Modifier
                .constrainAs(editView) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }) {
                AnimatedVisibility(
                    isAvailableShowComment,
                    enter = slideInVertically(initialOffsetY = { it / 2 })
                ) {
                    CommentEditTextView2(
                        originText = commentText.value,
                        newTaggedInfo = newTaggedText.value,
                        isLiked = detailInfo.isLiked,
                        commentEvent = {
                            when (it) {
                                is OpenDetailEditTextViewEvent.BucketLike -> {
                                    updateInternalEvent(OpenBucketDetailInternalEvent.ViewModelEvent.BucketLike)
                                }
                                is OpenDetailEditTextViewEvent.SendComment -> {
                                    updateInternalEvent(
                                        OpenBucketDetailInternalEvent.ViewModelEvent.AddComment(
                                            AddBucketCommentRequest(
                                                bucketlistId = detailInfo.bucketId.toInt(),
                                                content = it.sendText,
                                                mentionIds = "" //TODO : 수정필요
                                            )
                                        )
                                    )
                                    commentText.value = ""
                                }

                                is OpenDetailEditTextViewEvent.TextChanged -> {
                                    val textInfo = it
                                    commentText.value = textInfo.updateText
                                    newTaggedText.value = null

                                    taggedMemberList.clear()
                                    taggedMemberList.addAll(textInfo.taggedList.map { tagged -> tagged.id })

                                    // "@" 태그 위치 확인
                                    val tagIndexMap = mutableListOf<Int>()
                                    textInfo.updateText.mapIndexed { index, c ->
                                        if (c == '@') {
                                            tagIndexMap.add(index)
                                        }
                                    }

                                    // "@" 태그가 있는 경우, 현재 커서 위치와 비교해서 @__ 값 확인
                                    if (tagIndexMap.isNotEmpty()) {
                                        tagIndexMap.lastOrNull { index ->
                                            it.index > index
                                        }?.let { tagIndex ->

                                            // 커서의 위치가 전체 길이보다 뒤이면 리턴
                                            if (textInfo.index > textInfo.updateText.length) {
                                                mentionSearchInfo.value = null
                                                return@CommentEditTextView2
                                            }

                                            val needToSearchText =
                                                textInfo.updateText.substring(
                                                    tagIndex,
                                                    textInfo.index
                                                )

                                            if (needToSearchText.isNotEmpty()) {
                                                val info =
                                                    MentionSearchInfo(
                                                        needToSearchText,
                                                        tagIndex,
                                                        textInfo.index,
                                                        textInfo.taggedList
                                                    )

                                                mentionSearchInfo.value = info

                                            } else {
                                                mentionSearchInfo.value = null
                                            }

                                        }
                                    } else {
                                        mentionSearchInfo.value = null
                                    }
                                }
                            }
                        }

                    )
                }
            }

            mentionSearchInfo.value?.let { info ->
                if (info.searchText.isNotEmpty()) {
                    val removePrefixText = info.searchText.replace("@", "")
                    val searchedList =
                        validMentionList?.filter { it.nickName.contains(removePrefixText) }
                            .orEmpty()
                    if (searchedList.isNotEmpty()) {
                        MentionSearchListPopup(
                            searchedList = searchedList,
                            mentionSelected = {

                                var makeTaggedText = commentText.value
                                val nickName = "@" + it.nickName + " "

                                val rangeEndIndex =
                                    if (info.endIndex < makeTaggedText.length) info.endIndex + 1 else info.endIndex
                                makeTaggedText = makeTaggedText.replaceRange(
                                    info.startIndex,
                                    rangeEndIndex,
                                    nickName
                                )

                                val currentTime = LocalTime.now()
                                val timeString = currentTime.toString().substring(0, 8)

                                newTaggedText.value =
                                    TaggedTextInfo(
                                        id = "${info.startIndex}${timeString}",
                                        text = nickName,
                                        startIndex = info.startIndex,
                                        endIndex = info.startIndex + nickName.length - 1
                                    )
                                commentText.value = makeTaggedText
                                mentionSearchInfo.value = null
                            }
                        )
                    }
                }
            }
        }
    }

    ShowOptionEvent(
        internalEvent = internalEvent,
        commentList = detailInfo.commentInfo?.commentList.orEmpty(),
        updateInternalEvent = updateInternalEvent,
    )

    if (internalEvent is OpenBucketDetailInternalEvent.BucketMore) {
        handleMoreEvent(
            context = context,
            info = detailInfo,
            moreEvent = internalEvent,
            goToEvent = goToOutEvent,
            updateInternalEvent = updateInternalEvent
        )
    }
}

private fun handleMoreEvent(
    context: Context,
    info: BucketDetailUiInfo,
    moreEvent: OpenBucketDetailInternalEvent.BucketMore,
    goToEvent: (OpenBucketDetailEvent2) -> Unit,
    updateInternalEvent: (OpenBucketDetailInternalEvent) -> Unit
) {
    when (moreEvent) {
        is OpenBucketDetailInternalEvent.BucketMore.My -> {
            when (moreEvent.event) {
                is MyBucketMoreMenuEvent.GoToEdit -> {
                    goToEvent.invoke(
                        OpenBucketDetailEvent2.Update(
                            info.toUpdateUiModel(
                                createImageInfoWithPath(
                                    context, info.imageInfo?.imageList.orEmpty()
                                )
                            )
                        )
                    )
                }

                is MyBucketMoreMenuEvent.GoToGoalUpdate -> {
                    updateInternalEvent(
                        OpenBucketDetailInternalEvent.ShowUpdateGoalCountDialog(
                            info.descInfo.goalCount
                        )
                    )
                }

                is MyBucketMoreMenuEvent.GoToDelete -> {

                }
            }
        }

        is OpenBucketDetailInternalEvent.BucketMore.Other -> {
            when (moreEvent.event) {
                is OtherBucketMenuEvent.GoToHide -> {

                }

                is OtherBucketMenuEvent.GoToReport -> {
                    val reportInfo = ReportInfo(
                        id = info.bucketId,
                        writer = info.writerProfileInfo.nickName,
                        contents = info.descInfo.title,
                        reportType = ReportType.BUCKET
                    )
                    updateInternalEvent(OpenBucketDetailInternalEvent.ReportBucket(reportInfo))
                }
            }
        }
    }
}

@Composable
private fun ShowOptionEvent(
    commentList: List<Comment>,
    internalEvent: OpenBucketDetailInternalEvent,
    updateInternalEvent: (OpenBucketDetailInternalEvent) -> Unit
) {
    when (internalEvent) {
        is OpenBucketDetailInternalEvent.ReportBucket -> {
            ShowReportScreen(
                reportInfo = internalEvent.info,
                succeedReported = {
                    updateInternalEvent(
                        OpenBucketDetailInternalEvent.ViewModelEvent.UpdateBucket
                    )
                },
                closeEvent = {
                    updateInternalEvent(OpenBucketDetailInternalEvent.None)
                })
        }

        is OpenBucketDetailInternalEvent.ReportComment -> {
            ShowReportScreen(
                reportInfo = internalEvent.info,
                succeedReported = {
                    updateInternalEvent(
                        OpenBucketDetailInternalEvent.ViewModelEvent.UpdateBucket
                    )
                },
                closeEvent = {
                    updateInternalEvent(OpenBucketDetailInternalEvent.None)
                })
        }

        is OpenBucketDetailInternalEvent.ShowUpdateGoalCountDialog -> {
            GoalCountUpdateDialog(
                currentCount = internalEvent.count.toString()
            ) {
                when (it) {
                    GoalCountUpdateEvent.Close -> {
                        updateInternalEvent(OpenBucketDetailInternalEvent.None)
                    }

                    is GoalCountUpdateEvent.CountUpdate -> {
                        updateInternalEvent(
                            OpenBucketDetailInternalEvent.ViewModelEvent.UpdateGoalCount(
                                it.count
                            )
                        )
                    }
                }
            }
        }

        OpenBucketDetailInternalEvent.CommentTagListDialog -> TODO()
        is OpenBucketDetailInternalEvent.CommentOption -> {
            val commenter = commentList[internalEvent.index]

            if (commenter.isMine) {
                MyCommentSelectedDialog(
                    comment = commenter,
                    onDismissRequest = {
                        updateInternalEvent(OpenBucketDetailInternalEvent.None)
                    },
                    commentOptionClicked = {
                        when (it) {
                            is MyCommentOptionClicked.Delete -> {
                                updateInternalEvent(
                                    OpenBucketDetailInternalEvent.ViewModelEvent.DeleteMyComment(
                                        it.commentId
                                    )
                                )
                            }
                        }
                    }
                )
            } else {
                OtherCommentSelectedDialog(
                    comment = commenter,
                    onDismissRequest = {
                        updateInternalEvent(OpenBucketDetailInternalEvent.None)
                    },
                    commentOptionClicked = {
                        when (it) {
                            is OtherCommentOptionClicked.Hide -> TODO()
                            is OtherCommentOptionClicked.Report -> {
                                val reportInfo = ReportInfo(
                                    id = commenter.commentId,
                                    writer = commenter.nickName,
                                    contents = commenter.comment,
                                    reportType = ReportType.COMMENT
                                )
                                updateInternalEvent(
                                    OpenBucketDetailInternalEvent.ReportComment(
                                        reportInfo
                                    )
                                )
                            }
                        }
                    }
                )
            }
        }

        is OpenBucketDetailInternalEvent.MoreDialog -> {
            if (internalEvent.isMine) {
                MyDetailAppBarMoreMenuDialog(dismiss = {
                    updateInternalEvent(OpenBucketDetailInternalEvent.None)
                }, event = { event ->
                    updateInternalEvent(event)
                })

            } else {
                OtherDetailAppBarMoreMenuDialog(dismiss = {
                    updateInternalEvent(OpenBucketDetailInternalEvent.None)
                }, event = { event ->
                    updateInternalEvent(event)
                })
            }
        }

        is OpenBucketDetailInternalEvent.Error -> {
            ShowErrorDialog(
                internalEvent.status,
                achieve = {},
                backPress = {},
                updateInternalEvent = updateInternalEvent
            )
        }

//        OpenBucketDetailInternalEvent.Achieve -> TODO()
//        is OpenBucketDetailInternalEvent.AddComment -> TODO()

        else -> {
            // Do Nothing
        }
    }
}


@Composable
private fun ShowErrorDialog(
    event: DetailLoadFailStatus,
    achieve: () -> Unit,
    backPress: () -> Unit,
    updateInternalEvent: (OpenBucketDetailInternalEvent) -> Unit
) {
    val title = when (event) {
        DetailLoadFailStatus.AchieveFail -> stringResource(id = R.string.achieveBucketFail)
        DetailLoadFailStatus.LoadFail -> stringResource(id = R.string.loadBucketFail)
        else -> stringResource(id = R.string.apiFailTitle)
    }
    ApiFailDialog(
        title = title,
        message = stringResource(id = R.string.apiFailMessage)
    ) {
        when (event) {
            DetailLoadFailStatus.AchieveFail -> {
                achieve()
            }

            DetailLoadFailStatus.LoadFail -> {
                backPress()
            }

            else -> {
                // Do Nothing
            }
        }
        updateInternalEvent(OpenBucketDetailInternalEvent.None)
    }
}

@Preview(showBackground = true)
@Composable
private fun InternalOpenDetailScreenPreview() {

    InternalOpenDetailScreen(
        detailInfo = BucketDetailUiInfo(
            bucketId = "1",
            writeOpenType = WriteOpenType.PUBLIC,
            imageInfo = null,
            writerProfileInfo = DetailDescType.WriterProfileInfoUi(
                profileImage = null,
                badgeImage = "",
                titlePosition = "안녕나는한아야",
                nickName = "김한아",
                userId = "1"
            ),
            descInfo = DetailDescType.CommonDetailDescInfo(
                dDay = null,
                status = BucketStatus.PROGRESS,
                keywordList = listOf(),
                title = "타이틀!",
                goalCount = 10,
                userCount = 0,
                categoryInfo = WriteCategoryInfo(
                    id = 0,
                    name = "카테고리",
                    defaultYn = YesOrNo.N
                ),
                isScrap = false,
                isMine = true
            ),
            memoInfo = DetailDescType.MemoInfo("메모를 썼다고 해보자구\n아주긴ㄱㄹ로\n일단 스크롤이 되어야 하거든\n부탁 좀 핳게 !!!"),
            commentInfo = DetailDescType.CommentInfo(
                commentCount = 4402, commentList = listOf(
                    Comment(
                        commentId = "torquent",
                        userId = "11",
                        profileImage = "11111",
                        nickName = "Parker Montoya",
                        comment = "lorem",
                        isMine = false,
                        isBlocked = false
                    )
                )

            ),
            togetherInfo = null,
            isMine = true,
            isDone = false,
            isLiked = false
        ),
        validMentionList = emptyList(),
        internalEvent = OpenBucketDetailInternalEvent.None,
        goToOutEvent = {},
        updateInternalEvent = {},
        backPress = {}
    )
}