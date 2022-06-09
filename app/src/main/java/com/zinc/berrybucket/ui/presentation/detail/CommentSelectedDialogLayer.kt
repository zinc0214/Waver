package com.zinc.berrybucket.ui.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.Commenter
import com.zinc.berrybucket.ui.compose.theme.Gray1
import com.zinc.berrybucket.ui.compose.theme.Gray10

/**
 * 댓글 롱크릭 시 노출되는 팝업
 *
 * @param isMyComment 내 댓글인지 확인
 * @param commentInfo 댓글 정보
 * @param onDismissRequest
 * @param commentOptionClicked
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CommentSelectedDialogLayer(
    isMyComment: Boolean,
    commentInfo: Commenter?,
    onDismissRequest: (Boolean) -> Unit,
    commentOptionClicked: (CommentOptionClicked) -> Unit
) {

    val dialogProperties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        securePolicy = SecureFlagPolicy.Inherit,
        usePlatformDefaultWidth = false
    )

    if (commentInfo != null) {
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
                Text(
                    text = stringResource(id = R.string.commentOptionDialogTitle),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray10,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Text(
                    text = stringResource(id = if (isMyComment) R.string.commentEdit else R.string.commentHide),
                    fontSize = 14.sp,
                    color = Gray10,
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .fillMaxWidth()
                        .clickable {
                            commentOptionClicked.invoke(
                                CommentOptionClicked.FirstOptionClicked(
                                    commentInfo.commentId
                                )
                            )
                        }
                )
                Text(
                    text = stringResource(id = if (isMyComment) R.string.commentDelete else R.string.commentReport),
                    fontSize = 14.sp,
                    color = Gray10,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                        .clickable {
                            commentOptionClicked.invoke(
                                CommentOptionClicked.SecondOptionClicked(
                                    commentInfo.commentId
                                )
                            )
                        }
                )
            }
        }
    }
}

sealed class CommentOptionClicked {
    data class FirstOptionClicked(
        val commentId: String
    ) : CommentOptionClicked()

    data class SecondOptionClicked(
        val commentId: String
    ) : CommentOptionClicked()
}