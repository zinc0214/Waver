package com.zinc.berrybucket.ui.presentation.detail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
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
import com.zinc.berrybucket.model.CommentTagInfo
import com.zinc.berrybucket.ui.design.theme.Gray9
import com.zinc.berrybucket.ui.util.dpToSp

@Composable
fun DetailCommenterTagDropDownView(
    commentTaggableList: List<CommentTagInfo>,
    tagClicked: (CommentTagInfo) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(bottom = 56.dp)
            .wrapContentSize(Alignment.BottomCenter),
        shape = RoundedCornerShape(8.dp),
        elevation = 3.dp
    ) {
        Column {
            commentTaggableList.forEach {
                DropdownMenuItem(
                    onClick = {
                        tagClicked(it)
                    }, contentPadding = PaddingValues(0.dp)
                ) {
                    TagDropDownItem(
                        modifier = Modifier.fillMaxWidth(), commentTagInfo = it
                    ) { info ->
                        tagClicked.invoke(info)
                    }
                }
            }
        }
    }
}

@Composable
private fun TagDropDownItem(
    modifier: Modifier, commentTagInfo: CommentTagInfo, tagClicked: (CommentTagInfo) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable {
                tagClicked(commentTagInfo)
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

        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = commentTagInfo.nickName,
            color = Gray9,
            fontSize = dpToSp(13.dp),
            textAlign = TextAlign.Center
        )
    }
}