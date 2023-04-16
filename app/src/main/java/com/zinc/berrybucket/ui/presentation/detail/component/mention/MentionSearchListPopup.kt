package com.zinc.berrybucket.ui.presentation.detail.component.mention

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.CommentMentionInfo
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray9
import com.zinc.berrybucket.ui.presentation.common.MyText
import com.zinc.berrybucket.ui.util.dpToSp

@Composable
fun MentionSearchListPopup(
    searchedList: List<CommentMentionInfo>,
    mentionSelected: (CommentMentionInfo) -> Unit,
    modifier: Modifier
) {

    Card(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.BottomCenter)
            .padding(top = 8.dp, bottom = 8.dp),
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Gray1,
        elevation = 3.dp
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .background(color = Gray1)
                .height(150.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            items(items = searchedList, itemContent = {
                MentionItem(
                    modifier = Modifier.fillMaxWidth(),
                    item = it,
                    itemClicked = {
                        mentionSelected(it)
                    })
            })
        }
    }

}

@Composable
private fun MentionItem(
    modifier: Modifier = Modifier,
    item: CommentMentionInfo,
    itemClicked: (CommentMentionInfo) -> Unit
) {
    Row(
        modifier = modifier
            .background(color = Gray1)
            .padding(horizontal = 20.dp)
            .clickable {
                itemClicked(item)
            }, verticalAlignment = Alignment.CenterVertically
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

        MyText(
            modifier = Modifier.padding(start = 12.dp),
            text = item.nickName,
            color = Gray9,
            fontSize = dpToSp(13.dp),
            textAlign = TextAlign.Center
        )
    }
}