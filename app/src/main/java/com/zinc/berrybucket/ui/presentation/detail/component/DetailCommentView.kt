package com.zinc.berrybucket.ui.presentation.detail.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.CommentInfo
import com.zinc.berrybucket.model.Commenter
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Gray3
import com.zinc.berrybucket.ui.design.theme.Gray6
import com.zinc.berrybucket.ui.design.theme.Gray8
import com.zinc.berrybucket.ui.design.theme.Gray9
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.util.dpToSp

@Composable
fun DetailCommentView(
    commentInfo: CommentInfo?, commentLongClicked: (Int) -> Unit
) {
    Column(
        modifier = Modifier.padding(top = 20.dp)
    ) {

        if ((commentInfo?.commentCount ?: 0) > 0) {
            CommentListView(commentInfo?.commenterList.orEmpty(), commentLongClicked)
        } else {
            Spacer(modifier = Modifier.height(12.dp))
            CommentBlankView()
        }
    }
}

@Composable
fun CommentLine() {
    Divider(color = Gray3, thickness = 1.dp)
}

@Composable
fun CommentCountView(commentCount: Int) {
    Row(
        modifier = Modifier.padding(top = 28.dp, start = 20.dp)
    ) {
        Image(
            painter = painterResource(id = com.zinc.berrybucket.ui_common.R.drawable.btn_32_comment),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        MyText(
            text = "$commentCount",
            color = Gray10,
            fontSize = dpToSp(15.dp),
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
private fun CommentListView(commentList: List<Commenter>, commentLongClicked: (Int) -> Unit) {
    commentList.forEachIndexed { index, commenter ->
        CommentDescView(
            commenter = commenter,
            commentIndex = index,
            isLastItem = commentList.lastIndex == index,
            commentLongClicked = commentLongClicked
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CommentDescView(
    commenter: Commenter,
    commentIndex: Int,
    isLastItem: Boolean,
    commentLongClicked: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = if (isLastItem) 120.dp else 16.dp)
            .combinedClickable(
                onClick = { },
                onLongClick = {
                    commentLongClicked(commentIndex)
                },
            )
            .padding(start = 28.dp, end = 28.dp, top = 10.dp, bottom = 10.dp)
    ) {
        Image(
            painter = painterResource(id = com.zinc.berrybucket.ui_common.R.drawable.test),
            contentDescription = stringResource(
                id = com.zinc.berrybucket.ui_feed.R.string.feedProfileImage
            ),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(32.dp, 32.dp)
                .aspectRatio(1f)
                .clip(shape = RoundedCornerShape(12.dp))
        )

        Column(modifier = Modifier.padding(start = 12.dp)) {
            MyText(
                text = commenter.nickName,
                color = Gray9,
                fontSize = dpToSp(13.dp),
            )
            MyText(
                text = commenter.comment,
                color = Gray8,
                fontSize = dpToSp(14.dp),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun CommentBlankView() {
    MyText(
        modifier = Modifier
            .padding(horizontal = 28.dp)
            .padding(bottom = 140.dp),
        text = stringResource(R.string.commentBlankText),
        fontSize = dpToSp(14.dp),
        color = Gray6
    )
}

@Preview
@Composable
fun CommentBlankPreview() {
    CommentBlankView()
}