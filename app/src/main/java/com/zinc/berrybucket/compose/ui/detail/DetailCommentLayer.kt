package com.zinc.berrybucket.compose.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.*
import com.zinc.berrybucket.model.CommentInfo
import com.zinc.berrybucket.model.Commenter

@Composable
fun DetailCommentLayer(commentInfo: CommentInfo, commentLongClicked: (String) -> Unit) {
    Column(
        modifier = Modifier.padding(top = 20.dp)
    ) {
        CommentLine()
        CommentCountView(commentInfo.commentCount)

        if (commentInfo.commentCount > 0) {
            Spacer(modifier = Modifier.height(28.dp))
            CommentListView(commentInfo.commenterList, commentLongClicked)
        } else {
            Spacer(modifier = Modifier.height(12.dp))
            CommentBlankView()
        }
    }
}

@Composable
private fun CommentLine() {
    Divider(color = Gray3, thickness = 1.dp)
}

@Composable
private fun CommentCountView(commentCount: Int) {
    Row(
        modifier = Modifier.padding(top = 28.dp, start = 20.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.btn_32_comment),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = "$commentCount",
            color = Gray10,
            fontSize = 15.sp,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
private fun CommentListView(commentList: List<Commenter>, commentLongClicked: (String) -> Unit) {
    commentList.forEach { commenter ->
        CommentDescView(commenter = commenter, commentLongClicked = commentLongClicked)
    }
}

@Composable
private fun CommentDescView(commenter: Commenter, commentLongClicked: (String) -> Unit) {
    // val interactionSource = remember { MutableInteractionSource() }
    var isPressed = false
    val commentColor = if (isPressed) Gray3 else Gray1

    Row(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        commentLongClicked(commenter.commentId)
                    }
                )
            }
            .padding(start = 28.dp, bottom = 36.dp, end = 28.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.test),
            contentDescription = stringResource(
                id = R.string.feedProfileImage
            ),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(32.dp, 32.dp)
                .aspectRatio(1f)
                .clip(shape = RoundedCornerShape(12.dp))
        )

        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(
                text = commenter.nickName,
                color = Gray9,
                fontSize = 13.sp
            )
            Text(
                text = commenter.comment,
                color = Gray8,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun CommentBlankView() {
    Text(
        modifier = Modifier
            .padding(horizontal = 28.dp)
            .padding(bottom = 36.dp),
        text = stringResource(R.string.commentBlankText),
        fontSize = 14.sp,
        color = Gray6
    )
}
