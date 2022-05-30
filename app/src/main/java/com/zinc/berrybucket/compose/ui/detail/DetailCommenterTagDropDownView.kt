package com.zinc.berrybucket.compose.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.Gray9
import com.zinc.berrybucket.model.CommentTagInfo

@Composable
fun DetailCommenterTagDropDownView(
    modifier: Modifier = Modifier,
    commentTaggableList: List<CommentTagInfo>,
    tagClicked: (CommentTagInfo) -> Unit
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    var isExpanded by remember { mutableStateOf(commentTaggableList.isNotEmpty()) }

    MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(8.dp))) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .width(screenWidth - 32.dp)
                .wrapContentSize(Alignment.BottomCenter)
        ) {
            DropdownMenu(
                modifier = Modifier
                    .width(screenWidth - 32.dp),
                onDismissRequest = {
                    isExpanded = false
                },
                expanded = isExpanded,
                properties = PopupProperties(clippingEnabled = false),
                offset = DpOffset(0.dp, 8.dp)
            ) {
                commentTaggableList.forEach {
                    DropdownMenuItem(
                        onClick = {
                            tagClicked(it)
                        },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        TagDropDownItem(
                            modifier = Modifier.fillMaxWidth(),
                            commentTagInfo = it
                        ) { info ->
                            tagClicked.invoke(info)
                        }
                    }
                }
            }
        }

    }
}

@Composable
private fun TagDropDownItem(
    modifier: Modifier,
    commentTagInfo: CommentTagInfo,
    tagClicked: (CommentTagInfo) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable {
                tagClicked(commentTagInfo)
            },
        verticalAlignment = Alignment.CenterVertically
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
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}