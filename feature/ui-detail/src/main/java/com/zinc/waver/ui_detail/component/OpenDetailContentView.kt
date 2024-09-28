package com.zinc.waver.ui_detail.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zinc.waver.model.BucketDetailUiInfo
import com.zinc.waver.model.CommentLongClicked
import com.zinc.waver.model.DetailClickEvent
import com.zinc.waver.model.SuccessButtonInfo
import com.zinc.waver.ui.presentation.component.ImageViewPagerInsideIndicator

@Composable
fun OpenDetailContentView(
    modifier: Modifier,
    listState: LazyListState,
    info: BucketDetailUiInfo,
    flatButtonVisible: Boolean,
    clickEvent: (DetailClickEvent) -> Unit
) {

    val successButtonHide =
        listState.layoutInfo.visibleItemsInfo.isEmpty() || info.isMine.not() || info.isDone

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
            DetailProfileView(info.writerProfileInfo) {
                clickEvent(DetailClickEvent.GoToOtherProfile(it))
            }
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
            if (successButtonHide) {
                return@item
            }
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