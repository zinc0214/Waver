package com.zinc.berrybucket.ui.presentation.detail.component.mention

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.CommentMentionInfo
import com.zinc.berrybucket.ui.design.theme.Gray4
import com.zinc.berrybucket.ui.design.theme.Main1
import com.zinc.berrybucket.ui.design.theme.Main2
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.presentation.common.IconButton
import com.zinc.berrybucket.ui.presentation.common.MyText
import com.zinc.berrybucket.ui.util.dpToSp

@Composable
internal fun MentionedListView(
    mentionedList: List<CommentMentionInfo>,
    removeMention: (CommentMentionInfo) -> Unit
) {

    val localDensity = LocalDensity.current

    var columnHeightDp by remember {
        mutableStateOf(0.dp)
    }

    var fontSize by remember {
        mutableStateOf(20.dp)
    }

    var iconSize by remember {
        mutableStateOf(28.dp)
    }

    var currentSelectedSize by remember {
        mutableStateOf(0)
    }

    Column {
        Box {
            Icon(
                modifier = Modifier
                    .padding(start = 19.dp)
                    .padding(top = 4.dp)
                    .size(32.dp),
                painter = painterResource(id = R.drawable.btn_32_mention),
                contentDescription = stringResource(
                    id = R.string.commentButtonDesc
                ),
                tint = Main4
            )

            Column(
                modifier = Modifier
                    .heightIn(max = 164.dp)
                    .padding(top = 4.dp)
                    .padding(horizontal = 24.dp)
            ) {
                FlowRow(
                    modifier = Modifier
                        .onGloballyPositioned { coordinates ->
                            val updatedHeight =
                                with(localDensity) { coordinates.size.height.toDp() }

                            if (currentSelectedSize != mentionedList.size) {

                                var newTextSize = fontSize
                                var newIconSize = iconSize

                                if (mentionedList.size < 3) {
                                    newTextSize = 22.dp
                                    newIconSize = 28.dp
                                } else if (updatedHeight - columnHeightDp > 10.dp) {
                                    newTextSize =
                                        fontSize - (0.05 * (updatedHeight - columnHeightDp))
                                    newIconSize =
                                        iconSize - (0.05 * (updatedHeight - columnHeightDp))
                                } else if (columnHeightDp - updatedHeight > 10.dp) {
                                    newTextSize =
                                        fontSize + (0.05 * (columnHeightDp - updatedHeight))
                                    newIconSize =
                                        iconSize + (0.05 * (columnHeightDp - updatedHeight))
                                }

                                fontSize =
                                    if (newTextSize > 22.dp) 22.dp else if (newTextSize < 14.dp) 14.dp else newTextSize

                                iconSize =
                                    if (newIconSize > 28.dp) 28.dp else if (newIconSize < 22.dp) 22.dp else newIconSize

                                columnHeightDp = updatedHeight
                                currentSelectedSize = mentionedList.size
                            }
                        }
                        .fillMaxWidth(),
                    mainAxisSpacing = 6.dp,
                    crossAxisSpacing = 8.dp,
                    mainAxisSize = SizeMode.Expand
                ) {

                    Spacer(
                        modifier = Modifier
                            .width(28.dp)
                            .height(4.dp)
                    )

                    mentionedList.forEach { item ->
                        CommentSelectedItemView(commenter = item, textSize = fontSize,
                            iconSize = iconSize,
                            delete = { removeMention(item) })
                    }
                }
            }
        }

        Divider(
            color = Gray4,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 12.5.dp)
                .height(1.dp)
        )
    }
}

@Composable
private fun CommentSelectedItemView(
    commenter: CommentMentionInfo,
    textSize: Dp,
    iconSize: Dp,
    delete: (CommentMentionInfo) -> Unit
) {

    Row(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = Main1),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MyText(
            text = commenter.nickName,
            fontSize = dpToSp(dp = textSize),
            color = Main4,
            modifier = Modifier.padding(start = 6.dp, top = 1.dp, bottom = 3.dp, end = 4.dp)
        )
        IconButton(
            onClick = { delete(commenter) },
            image = R.drawable.btn_20_delete,
            contentDescription = "지우기",
            modifier = Modifier
                .padding(end = 2.dp)
                .size(iconSize),
            colorFilter = ColorFilter.tint(Main2)
        )
    }
}

@Composable
@Preview
private fun CommentSelectedItemPreview() {
    CommentSelectedItemView(
        commenter = CommentMentionInfo(
            userId = "1",
            profileImage = "",
            nickName = "김성철 절대 엘지켜",
            isFriend = false,
            isSelected = false
        ), textSize = 22.dp, delete = {}, iconSize = 20.dp
    )
}

@Composable
@Preview
private fun MentionTextPreview() {
    val tagableNickName = mutableListOf<CommentMentionInfo>()

    repeat(4) {
        tagableNickName.add(
            CommentMentionInfo(
                userId = "$it",
                profileImage = "",
                nickName = "가나다 $it",
                isFriend = false,
                isSelected = false
            )
        )
    }
    MentionedListView(mentionedList = tagableNickName, removeMention = {})
}