package com.zinc.berrybucket.ui.presentation.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.Comment
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.util.dpToSp

/**
 * 댓글 롱크릭 시 노출되는 팝업
 *
 * @param comment 댓글 정보
 * @param onDismissRequest
 * @param commentOptionClicked
 */
@Composable
fun MyCommentSelectedDialog(
    comment: Comment,
    onDismissRequest: (Boolean) -> Unit,
    commentOptionClicked: (MyCommentOptionClicked) -> Unit
) {
    CommentSelectedDialog(comment, onDismissRequest) {
        when (it) {
            is InnerCommentOptionClicked.FirstOptionClickedInner ->
                commentOptionClicked(MyCommentOptionClicked.Edit(it.commentId))

            is InnerCommentOptionClicked.SecondOptionClickedInner ->
                commentOptionClicked(MyCommentOptionClicked.Delete(it.commentId))
        }
    }
}

/**
 * 댓글 롱크릭 시 노출되는 팝업
 *
 * @param comment 댓글 정보
 * @param onDismissRequest
 * @param commentOptionClicked
 */
@Composable
fun OtherCommentSelectedDialog(
    comment: Comment,
    onDismissRequest: (Boolean) -> Unit,
    commentOptionClicked: (OtherCommentOptionClicked) -> Unit
) {
    CommentSelectedDialog(comment, onDismissRequest) {
        when (it) {
            is InnerCommentOptionClicked.FirstOptionClickedInner ->
                commentOptionClicked(OtherCommentOptionClicked.Hide(it.commentId))

            is InnerCommentOptionClicked.SecondOptionClickedInner ->
                commentOptionClicked(OtherCommentOptionClicked.Report(it.commentId))
        }
    }
}


@Composable
private fun CommentSelectedDialog(
    comment: Comment,
    onDismissRequest: (Boolean) -> Unit,
    commentOptionClicked: (InnerCommentOptionClicked) -> Unit
) {

    val dialogProperties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        securePolicy = SecureFlagPolicy.Inherit,
        usePlatformDefaultWidth = false
    )


    Dialog(
        properties = dialogProperties,
        onDismissRequest = { onDismissRequest(false) }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp)
                .background(color = Gray1, shape = RoundedCornerShape(8.dp))
                .padding(24.dp)
        ) {
            MyText(
                text = stringResource(id = R.string.commentOptionDialogTitle),
                fontSize = dpToSp(14.dp),
                fontWeight = FontWeight.Bold,
                color = Gray10,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            MyText(
                text = stringResource(id = if (comment.isMine) R.string.commentEdit else R.string.commentHide),
                fontSize = dpToSp(14.dp),
                color = Gray10,
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth()
                    .clickable {
                        commentOptionClicked.invoke(
                            InnerCommentOptionClicked.FirstOptionClickedInner(
                                comment.userId
                            )
                        )
                    }
            )
            MyText(
                text = stringResource(id = if (comment.isMine) R.string.commentDelete else R.string.commentReport),
                fontSize = dpToSp(14.dp),
                color = Gray10,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
                    .clickable {
                        commentOptionClicked.invoke(
                            InnerCommentOptionClicked.SecondOptionClickedInner(
                                comment.userId
                            )
                        )
                    }
            )
        }
    }
}


private sealed class InnerCommentOptionClicked {
    data class FirstOptionClickedInner(
        val commentId: String
    ) : InnerCommentOptionClicked()

    data class SecondOptionClickedInner(
        val commentId: String
    ) : InnerCommentOptionClicked()
}


sealed interface MyCommentOptionClicked {
    data class Edit(val commentId: String) : MyCommentOptionClicked
    data class Delete(val commentId: String) : MyCommentOptionClicked
}

sealed interface OtherCommentOptionClicked {
    data class Hide(val commentId: String) : OtherCommentOptionClicked
    data class Report(val commentId: String) : OtherCommentOptionClicked
}
