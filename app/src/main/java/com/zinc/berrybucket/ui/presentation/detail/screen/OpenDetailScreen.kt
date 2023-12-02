package com.zinc.berrybucket.ui.presentation.detail.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.berrybucket.model.BucketDetailUiInfo
import com.zinc.berrybucket.model.CommentLongClicked
import com.zinc.berrybucket.model.DetailAppBarClickEvent
import com.zinc.berrybucket.model.DetailClickEvent
import com.zinc.berrybucket.model.MyProfileInfoUi
import com.zinc.berrybucket.model.SuccessButtonInfo
import com.zinc.berrybucket.model.UserSelectedImageInfo
import com.zinc.berrybucket.ui.design.theme.BaseTheme
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.util.Keyboard
import com.zinc.berrybucket.ui.design.util.keyboardAsState
import com.zinc.berrybucket.ui.design.util.visibleLastIndex
import com.zinc.berrybucket.ui.presentation.component.ImageViewPagerInsideIndicator
import com.zinc.berrybucket.ui.presentation.component.ProfileView
import com.zinc.berrybucket.ui.presentation.detail.DetailViewModel
import com.zinc.berrybucket.ui.presentation.detail.component.CommentCountView
import com.zinc.berrybucket.ui.presentation.detail.component.CommentLine
import com.zinc.berrybucket.ui.presentation.detail.component.CommentOptionClicked
import com.zinc.berrybucket.ui.presentation.detail.component.CommentSelectedDialog
import com.zinc.berrybucket.ui.presentation.detail.component.DetailCommentView
import com.zinc.berrybucket.ui.presentation.detail.component.DetailDescView
import com.zinc.berrybucket.ui.presentation.detail.component.DetailMemoView
import com.zinc.berrybucket.ui.presentation.detail.component.DetailSuccessButtonView
import com.zinc.berrybucket.ui.presentation.detail.component.DetailTopAppBar
import com.zinc.berrybucket.ui.presentation.detail.component.GoalCountUpdateDialog
import com.zinc.berrybucket.ui.presentation.detail.component.MyDetailAppBarMoreMenuDialog
import com.zinc.berrybucket.ui.presentation.detail.component.OtherDetailAppBarMoreMenuDialog
import com.zinc.berrybucket.ui.presentation.detail.component.TogetherMemberView
import com.zinc.berrybucket.ui.presentation.detail.component.mention.CommentEditTextView2
import com.zinc.berrybucket.ui.presentation.detail.component.mention.MentionSearchListPopup
import com.zinc.berrybucket.ui.presentation.detail.model.GoalCountUpdateEvent
import com.zinc.berrybucket.ui.presentation.detail.model.MentionSearchInfo
import com.zinc.berrybucket.ui.presentation.detail.model.OpenDetailEditTextViewEvent
import com.zinc.berrybucket.ui.presentation.detail.model.TaggedTextInfo
import com.zinc.berrybucket.ui.presentation.detail.model.toUpdateUiModel
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.util.createImageInfoWithPath
import com.zinc.berrybucket.util.nav.GoToBucketDetailEvent
import com.zinc.common.models.ReportInfo
import java.time.LocalTime

@Composable
fun OpenDetailScreen(
    detailId: String,
    isMine: Boolean,
    goToEvent: (GoToBucketDetailEvent) -> Unit,
    backPress: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: DetailViewModel = hiltViewModel()

    viewModel.getValidMentionList()

    val vmDetailInfoAsState by viewModel.bucketBucketDetailUiInfo.observeAsState()
    val validMentionList by viewModel.validMentionList.observeAsState()
    val imageInfos = remember { mutableListOf<UserSelectedImageInfo>() }

    val detailInfo = remember {
        mutableStateOf(vmDetailInfoAsState)
    }

    if (detailInfo.value == null) {
        viewModel.getBucketDetail(detailId, isMine)
    }

    LaunchedEffect(key1 = vmDetailInfoAsState) {
        detailInfo.value = vmDetailInfoAsState
    }

    detailInfo.value?.let { info ->

        // scroll 상태에 따른 버튼 상태
        val listScrollState = rememberLazyListState()
        val titleIndex = if (info.imageInfo == null) 1 else 2 // 타이틀의 위치
        val flatButtonIndex = flatButtonIndex(info) // 붙는 버튼의 위치
        val visibleLastIndex = listScrollState.visibleLastIndex() // 현재 보여지는 마지막 아이템의 index
        val isCommentViewShown =
            visibleLastIndex > listScrollState.layoutInfo.totalItemsCount - 2 // 댓글이 보이는지 여부 ( -2 == 댓글이 마지막이고, 그 전의 카운터에서부터 노출하기 위해)
        val isScrollable =
            if (visibleLastIndex == 0) true else visibleLastIndex <= listScrollState.layoutInfo.totalItemsCount // 미자믹 아이템  == 전체아이템 갯수 인지 확인


        // 붙은버튼의 노출조건
        // 1. 댓글이 노출되는 경우
        // 2. 친구 또는 댓글의 영역이 노출되는 경우 (현재 마지막으로 보이는 Index 가 (2) 의 Index 인 경우)

        val flatButtonVisible = isCommentViewShown || visibleLastIndex >= flatButtonIndex

        // 팝업 노출 여부
        val optionPopUpShowed = remember { mutableStateOf(false) } // 우상단 옵션 팝업 노출 여부
        val commentOptionPopUpShowed =
            remember { mutableStateOf(false to 0) } // 댓글 롱클릭 옵션 팝업 노출여부 + 댓글 index
        val mentionPopupShowed = remember { mutableStateOf(false) } // 댓글 태그 팝업 노출 여부
        val goalCountUpdatePopUpShowed = remember { mutableStateOf(false) } // 달성횟수 팝업 노출 여부

        // 키보드 상태 확인
        val isKeyBoardOpened = remember { mutableStateOf(true) } // 키보드 오픈 상태 확인
        val isKeyboardStatus by keyboardAsState()
        val focusManager = LocalFocusManager.current
        if (isKeyboardStatus == Keyboard.Closed) {
            focusManager.clearFocus()
            isKeyBoardOpened.value = false
            mentionPopupShowed.value = false
        } else {
            isKeyBoardOpened.value = true
        }

        // 전체 뷰 clickable 인데, 리플 효과 제거를 위해 사용
        val interactionSource = remember { MutableInteractionSource() }

        // 댓글 값 저장
        val commentText = remember { mutableStateOf("") }

        // 새로 선택된 태그 정보
        val newTaggedText: MutableState<TaggedTextInfo?> = remember { mutableStateOf(null) }

        // 검색할 텍스트와 관련된 정보들
        val mentionSearchInfo: MutableState<MentionSearchInfo?> = remember { mutableStateOf(null) }

        if (imageInfos.isEmpty()) {
            imageInfos.addAll(createImageInfoWithPath(context, info.imageInfo?.imageList.orEmpty()))
        }

        BaseTheme {
            Scaffold { padding ->
                if (optionPopUpShowed.value) {
                    if (info.isMine) {
                        MyDetailAppBarMoreMenuDialog(optionPopUpShowed) {
                            when (it) {
                                MyBucketMenuEvent.GoToDelete -> {
                                    optionPopUpShowed.value = false
                                }

                                MyBucketMenuEvent.GoToEdit -> {
                                    optionPopUpShowed.value = false
                                    goToEvent.invoke(
                                        GoToBucketDetailEvent.GoToUpdate(
                                            info.toUpdateUiModel(
                                                imageInfos
                                            )
                                        )
                                    )
                                }

                                MyBucketMenuEvent.GoToGoalUpdate -> {
                                    goalCountUpdatePopUpShowed.value = true
                                    optionPopUpShowed.value = false
                                }
                            }
                        }
                    } else {
                        OtherDetailAppBarMoreMenuDialog(optionPopUpShowed = optionPopUpShowed) { event ->
                            when (event) {
                                OtherBucketMenuEvent.GoToHide -> {

                                }

                                OtherBucketMenuEvent.GoToReport -> {

                                }
                            }
                        }
                    }
                }

                if (goalCountUpdatePopUpShowed.value) {
                    GoalCountUpdateDialog(
                        currentCount = info.descInfo.goalCount.toString()
                    ) {
                        when (it) {
                            GoalCountUpdateEvent.Close -> {
                                goalCountUpdatePopUpShowed.value = false
                            }

                            is GoalCountUpdateEvent.CountUpdate -> {
                                // Todo : ViewModel Update!
                                viewModel.goalCountUpdate(info.bucketId, it.count)
                                goalCountUpdatePopUpShowed.value = false
                            }
                        }
                    }
                }
                if (commentOptionPopUpShowed.value.first) {
                    val commenter = vmDetailInfoAsState?.commentInfo?.commenterList?.getOrNull(
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
                        title = info.descInfo.title,
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

                        ContentView(
                            listState = listScrollState,
                            info = info,
                            clickEvent = {
                                when (it) {
                                    is CommentLongClicked -> {
                                        commentOptionPopUpShowed.value = true to it.commentIndex
                                    }

                                    is DetailClickEvent.SuccessClicked -> {
                                        viewModel.achieveMyBucket(it.id, isMine)
                                    }
                                }
                            },
                            flatButtonVisible = flatButtonVisible,
                            modifier = Modifier
                                .constrainAs(contentView) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    height = Dimension.fillToConstraints
                                })

                        // 플로팅 완료 버튼 노출 조건
                        if (listScrollState.layoutInfo.visibleItemsInfo.isNotEmpty() && info.isMine) {
                            this@Column.AnimatedVisibility(
                                flatButtonVisible.not(),
                                enter = expandVertically(),
                                exit = shrinkVertically(),
                                modifier = Modifier
                                    .constrainAs(
                                        floatingButtonView
                                    ) {
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                        bottom.linkTo(parent.bottom)
                                    }
                            ) {
                                DetailSuccessButtonView(
                                    modifier = Modifier.padding(bottom = 30.dp),
                                    successClicked = {
                                        viewModel.achieveMyBucket(info.bucketId, isMine)
                                    },
                                    successButtonInfo = SuccessButtonInfo(
                                        goalCount = info.descInfo.goalCount,
                                        userCount = info.descInfo.userCount
                                    )
                                )
                            }
                        }


                        // 최하단 EditTextView
                        Column(modifier = Modifier
                            .constrainAs(editView) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            }) {
                            AnimatedVisibility(isCommentViewShown || !isScrollable) {
                                CommentEditTextView2(
                                    originText = commentText.value,
                                    newTaggedInfo = newTaggedText.value,
                                    commentEvent = {
                                        when (it) {
                                            is OpenDetailEditTextViewEvent.SendComment -> {

                                            }

                                            is OpenDetailEditTextViewEvent.TextChanged -> {
                                                val textInfo = it
                                                commentText.value = textInfo.updateText
                                                newTaggedText.value = null

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
                                                            val info = MentionSearchInfo(
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

                                            newTaggedText.value = TaggedTextInfo(
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
            }
        }
    }
}


@Composable
private fun ContentView(
    modifier: Modifier,
    listState: LazyListState,
    info: BucketDetailUiInfo,
    flatButtonVisible: Boolean,
    clickEvent: (DetailClickEvent) -> Unit
) {

    LazyColumn(
        modifier = modifier, state = listState
    ) {
        info.imageInfo?.let {
            item(key = "imageViewPager") {
                ImageViewPagerInsideIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    indicatorModifier = Modifier.padding(bottom = 16.dp, end = 16.dp),
                    imageList = it.imageList
                )
            }
        }

        item(key = "profileView") {
            ProfileView(info.myProfileInfo)
        }

        item(key = "detailDescLayer") {
            DetailDescView(info.descInfo)
        }

        item(key = "memoView") {
            if (info.memoInfo != null) {
                DetailMemoView(
                    modifier = Modifier.padding(
                        top = 24.dp, start = 28.dp, end = 28.dp, bottom = 56.dp
                    ), memo = info.memoInfo?.memo!!
                )
            } else {
                Spacer(modifier = Modifier.height(56.dp))
            }

        }

        item(key = "flatDetailSuccessButton") {
            if (listState.layoutInfo.visibleItemsInfo.isEmpty() || info.isMine.not()) {
                return@item
            }
            // Box(modifier = Modifier.alpha(if (flatButtonVisible) 1f else 0f)) {
            AnimatedVisibility(
                flatButtonVisible,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                DetailSuccessButtonView(
                    successClicked = {
                        clickEvent.invoke(DetailClickEvent.SuccessClicked(info.bucketId))
                    }, successButtonInfo = SuccessButtonInfo(
                        goalCount = info.descInfo.goalCount,
                        userCount = info.descInfo.userCount
                    )
                )
            }
            // }
        }

        item(key = "friendsView") {
            if (info.togetherInfo != null) {
                TogetherMemberView(
                    modifier = Modifier.fillMaxWidth(),
                    togetherInfo = info.togetherInfo!!
                )
            }
        }

        item(key = "spacer") {
            Spacer(modifier = Modifier.height(30.dp))
        }
        item(key = "commentLine") {
            CommentLine()
        }
        item(key = "commentCountView") {
            CommentCountView(info.commentInfo?.commentCount ?: 0)
        }
        item(key = "commentLayer") {
            DetailCommentView(commentInfo = info.commentInfo, commentLongClicked = {
                clickEvent.invoke(CommentLongClicked(it))
            })
        }
    }
}

@Composable
fun ProfileView(myProfileInfo: MyProfileInfoUi) {
    ProfileView(
        modifier = Modifier
            .padding(top = 28.dp)
            .padding(horizontal = 12.dp),
        imageSize = 48.dp,
        profileSize = 44.dp,
        profileRadius = 16.dp,
        badgeSize = Pair(24.dp, 27.dp),
        nickNameTextSize = dpToSp(14.dp),
        titlePositionTextSize = dpToSp(13.dp),
        nickNameTextColor = Gray10,
        profileInfo = myProfileInfo.toUi()
    )
}

private fun flatButtonIndex(bucketDetailUiInfo: BucketDetailUiInfo): Int {
    var index = 2
    if (bucketDetailUiInfo.imageInfo != null) {
        index += 1
    }
    if (bucketDetailUiInfo.memoInfo != null) {
        index += 1
    }
    return index
}