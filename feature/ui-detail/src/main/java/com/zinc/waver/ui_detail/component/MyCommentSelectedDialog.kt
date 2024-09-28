package com.zinc.waver.ui_detail.component

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
import com.zinc.waver.model.Comment
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_detail.R

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
                text = stringResource(id = R.string.commentDelete),
                fontSize = dpToSp(14.dp),
                color = Gray10,
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth()
                    .clickable {
                        commentOptionClicked.invoke(MyCommentOptionClicked.Delete(comment.commentId))
                    }
            )
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
                text = stringResource(R.string.commentHide),
                fontSize = dpToSp(14.dp),
                color = Gray10,
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth()
                    .clickable {
                        commentOptionClicked.invoke(
                            OtherCommentOptionClicked.Hide(
                                comment.commentId
                            )
                        )
                    }
            )
            MyText(
                text = stringResource(R.string.commentReport),
                fontSize = dpToSp(14.dp),
                color = Gray10,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
                    .clickable {
                        commentOptionClicked.invoke(
                            OtherCommentOptionClicked.Report(
                                comment.commentId
                            )
                        )
                    }
            )
        }
    }
}


sealed interface MyCommentOptionClicked {
    data class Delete(val commentId: String) : MyCommentOptionClicked
}

sealed interface OtherCommentOptionClicked {
    data class Hide(val commentId: String) : OtherCommentOptionClicked
    data class Report(val commentId: String) : OtherCommentOptionClicked
}
