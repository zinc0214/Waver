package com.zinc.berrybucket.ui.presentation.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.Commenter
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.util.dpToSp

/**
 * 댓글 롱크릭 시 노출되는 팝업
 *
 * @param commenter 댓글 정보
 * @param onDismissRequest
 * @param commentOptionClicked
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CommentSelectedDialog(
    commenter: Commenter,
    onDismissRequest: (Boolean) -> Unit,
    commentOptionClicked: (CommentOptionClicked) -> Unit
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
                text = stringResource(id = if (commenter.isMine) R.string.commentEdit else R.string.commentHide),
                fontSize = dpToSp(14.dp),
                color = Gray10,
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth()
                    .clickable {
                        commentOptionClicked.invoke(
                            CommentOptionClicked.FirstOptionClicked(
                                commenter.commentId
                            )
                        )
                    }
            )
            MyText(
                text = stringResource(id = if (commenter.isMine) R.string.commentDelete else R.string.commentReport),
                fontSize = dpToSp(14.dp),
                color = Gray10,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
                    .clickable {
                        commentOptionClicked.invoke(
                            CommentOptionClicked.SecondOptionClicked(
                                commenter.commentId
                            )
                        )
                    }
            )
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