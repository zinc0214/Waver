package com.zinc.waver.ui_detail.screen

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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
import com.zinc.waver.model.DialogButtonInfo
import com.zinc.waver.model.ReportInfo
import com.zinc.waver.model.ReportType
import com.zinc.waver.model.SuccessButtonInfo
import com.zinc.waver.model.WriteCategoryInfo
import com.zinc.waver.model.WriteOpenType
import com.zinc.waver.ui.design.theme.Gray7
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.design.util.Keyboard
import com.zinc.waver.ui.design.util.keyboardAsState
import com.zinc.waver.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.waver.ui.presentation.component.dialog.CommonDialogView
import com.zinc.waver.ui.util.WaverLoading
import com.zinc.waver.ui_common.R
import com.zinc.waver.ui_detail.component.CommentEditView
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
import com.zinc.waver.ui_detail.model.GoalCountUpdateEvent
import com.zinc.waver.ui_detail.model.MyBucketMoreMenuEvent
import com.zinc.waver.ui_detail.model.OpenBucketDetailEvent2
import com.zinc.waver.ui_detail.model.OpenBucketDetailInternalEvent
import com.zinc.waver.ui_detail.model.OtherBucketMenuEvent
import com.zinc.waver.ui_detail.model.toUpdateUiModel
import com.zinc.waver.ui_detail.viewmodel.DetailViewModel
import com.zinc.waver.util.createImageInfoWithPath
import com.zinc.waver.ui_detail.R as DetailR

@Composable
fun OpenDetailBucketListScreen(
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
    val bucketDeleteAsState by viewModel.goToBack.observeAsState()
    val showLoadingAsState by viewModel.showLoading.observeAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val showLoading = remember { mutableStateOf(false) }
    var detailInfo by remember { mutableStateOf(detailInfoAsState) }
    var internalEvent: OpenBucketDetailInternalEvent by remember {
        mutableStateOf(
            OpenBucketDetailInternalEvent.None
        )
    }

    LaunchedEffect(Unit) {
        viewModel.loadInitData(detailId, writerId, isMine)
    }

    LaunchedEffect(showLoadingAsState) {
        showLoading.value = showLoadingAsState == true
    }

    LaunchedEffect(loadFailAsState) {
        loadFailAsState?.let { internalEvent = OpenBucketDetailInternalEvent.Error(it) }
    }

    LaunchedEffect(detailInfoAsState) {
        detailInfoAsState?.let {
            detailInfo = it
        }
    }

    LaunchedEffect(bucketDeleteAsState) {
        if (bucketDeleteAsState == true) {
            backPress()
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
                    Log.e("ayhan", "AddComment : ${event.comment}")
                    viewModel.requestAddBucketComment(event.comment)
                    keyboardController?.hide()
                }

                OpenBucketDetailInternalEvent.ViewModelEvent.BucketLike -> {
                    viewModel.saveBucketLike()
                }

                is OpenBucketDetailInternalEvent.ViewModelEvent.HideComment -> {
                    viewModel.hideComment(event.commentId)
                }

                OpenBucketDetailInternalEvent.ViewModelEvent.DeleteBucket -> {
                    viewModel.deleteMyBucket()
                }

                OpenBucketDetailInternalEvent.ViewModelEvent.BlockUser -> {
                    viewModel.blockBucketWriter()
                }
            }

            internalEvent = OpenBucketDetailInternalEvent.None
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

    if (showLoading.value) {
        WaverLoading()
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

    val successButtonVisible by remember {
        derivedStateOf {
            listScrollState.layoutInfo.visibleItemsInfo
                .find { it.key == "successButton" }
                .isSuccessButtonVisible(listScrollState.layoutInfo.viewportSize.height)
        }
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
                            Log.e("ayhan", "GoToOtherProfile : ${it.id}")
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

            Column(
                modifier = Modifier.constrainAs(floatingButtonView) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            ) {
                AnimatedVisibility(
                    visible = !successButtonVisible,
                    exit = fadeOut() + shrinkVertically(),
                    enter = slideInVertically(initialOffsetY = { it })
                ) {
                    if (detailInfo.canShowCompleteButton && keyboardStatus == Keyboard.Closed) {
                        DetailSuccessButtonView(
                            modifier = Modifier
                                .padding(bottom = 28.dp),
                            successClicked = {
                                updateInternalEvent(OpenBucketDetailInternalEvent.ViewModelEvent.Achieve)
                            },
                            successButtonInfo = SuccessButtonInfo(
                                goalCount = detailInfo.descInfo.goalCount,
                                userCount = detailInfo.descInfo.userCount
                            )
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .constrainAs(editView) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }) {
                AnimatedVisibility(
                    successButtonVisible,
                    enter = slideInVertically(initialOffsetY = { it / 2 }),
                    exit = slideOutVertically(targetOffsetY = { it })
                ) {
                    CommentEditView(
                        onCommentChanged = {},
                        mentionableUsers = validMentionList,
                        sendCommend = {
                            updateInternalEvent(
                                OpenBucketDetailInternalEvent.ViewModelEvent.AddComment(
                                    AddBucketCommentRequest(
                                        bucketId = detailInfo.bucketId.toInt(),
                                        content = it,
                                        mentionIds = emptyList()
                                    )
                                )
                            )
                        },
                        isLike = detailInfo.isLike,
                        onLikeChanged = {
                            updateInternalEvent(OpenBucketDetailInternalEvent.ViewModelEvent.BucketLike)
                        }
                    )
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

private fun LazyListItemInfo?.isSuccessButtonVisible(viewportHeight: Int): Boolean {
    if (this == null) {
        return false
    }
    val topIsVisible = this.offset >= 0
    val bottomIsVisible = (this.offset + this.size) <= viewportHeight
    return topIsVisible && bottomIsVisible
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
                    updateInternalEvent(
                        OpenBucketDetailInternalEvent.ViewModelEvent.DeleteBucket
                    )
                }
            }
        }

        is OpenBucketDetailInternalEvent.BucketMore.Other -> {
            when (moreEvent.event) {
                is OtherBucketMenuEvent.GoToBlock -> {

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
                            is OtherCommentOptionClicked.Hide -> {
                                updateInternalEvent(
                                    OpenBucketDetailInternalEvent.ViewModelEvent.HideComment(
                                        commentId = it.commentId
                                    )
                                )
                            }

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

        is OpenBucketDetailInternalEvent.BlockUserCheck -> {
            CommonDialogView(
                message = stringResource(DetailR.string.userBlockConfirmTitle),
                dismissAvailable = false,
                leftButtonInfo = DialogButtonInfo(
                    text = R.string.cancel,
                    color = Gray7
                ),
                leftButtonEvent = {
                    updateInternalEvent(OpenBucketDetailInternalEvent.None)
                },
                rightButtonInfo = DialogButtonInfo(
                    text = R.string.block,
                    color = Main4
                ),
                rightButtonEvent = {
                    updateInternalEvent(OpenBucketDetailInternalEvent.ViewModelEvent.BlockUser)
                }
            )
        }

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
                badgeTitle = "안녕나는한아야",
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
            isLike = false
        ),
        validMentionList = emptyList(),
        internalEvent = OpenBucketDetailInternalEvent.None,
        goToOutEvent = {},
        updateInternalEvent = {},
        backPress = {}
    )
}
