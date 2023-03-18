package com.zinc.berrybucket.ui.presentation.detail.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.google.accompanist.flowlayout.FlowRow
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.Commenter
import com.zinc.berrybucket.ui.design.theme.Gray3
import com.zinc.berrybucket.ui.presentation.common.IconButton
import com.zinc.berrybucket.ui.presentation.common.MyText
import com.zinc.berrybucket.ui.util.dpToSp

@Composable
fun CommentSelectView() {

    val selectedFriends = remember {
        mutableStateListOf<Commenter>()
    }

    val selectableFriends = buildList {
        add(
            Commenter(
                commentId = "1", profileImage = "", nickName = "가나다라", comment = "", isMine = false
            )
        )
        add(
            Commenter(
                commentId = "2",
                profileImage = "",
                nickName = "예쁘고예쁜부비부",
                comment = "",
                isMine = false
            )
        )
        add(
            Commenter(
                commentId = "3", profileImage = "", nickName = "더글로리", comment = "", isMine = false
            )
        )
    }

    val localDensity = LocalDensity.current

    var columnHeightDp by remember {
        mutableStateOf(0.dp)
    }

    var fontSize by remember {
        mutableStateOf(20.dp)
    }

    var currentSelectedSize by remember {
        mutableStateOf(0)
    }

    Log.e("ayhan", "height :,  $columnHeightDp")

    Column {
        FlowRow(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    val updatedHeight = with(localDensity) { coordinates.size.height.toDp() }
                    if (currentSelectedSize != selectedFriends.size) {
                        if (updatedHeight - columnHeightDp > 0.dp) {
                            val newSize =
                                fontSize.value - (0.1 * (updatedHeight - columnHeightDp)).value
                            fontSize = newSize.dp
                        }
                        Log.e("ayhan", "fontSize : $fontSize")
                        columnHeightDp = updatedHeight
                        currentSelectedSize = selectedFriends.size
                    }

                }
                .fillMaxWidth(),
            mainAxisSpacing = 12.dp,
            crossAxisSpacing = 8.dp,
        ) {
            selectedFriends.forEach { item ->
                CommentSelectedItemView(commenter = item, textSize = fontSize, delete = { id ->
                    selectedFriends.removeAll { it.commentId == id }
                })
            }
        }

        Column {
            selectableFriends.forEach {
                CommentSelectableItemView(commenter = it, selected = { commenter ->
                    selectedFriends.add(commenter)
                })
            }
        }
    }
}

@Composable
private fun CommentSelectedItemView(commenter: Commenter, textSize: Dp, delete: (String) -> Unit) {
    Row(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(15.dp))
            .background(color = Gray3)
            .padding(horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MyText(text = commenter.nickName, fontSize = dpToSp(dp = textSize))
        IconButton(
            onClick = { delete(commenter.commentId) },
            image = R.drawable.btn_24_close,
            contentDescription = "지우기",
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun CommentSelectableItemView(commenter: Commenter, selected: (Commenter) -> Unit) {
    MyText(
        text = commenter.nickName, modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                selected(commenter)
            }
    )
}


@Preview
@Composable
private fun CommentSelectItemPreview() {
    CommentSelectedItemView(
        Commenter(
            commentId = "1", profileImage = "1", nickName = "안녕", comment = "", isMine = false
        ),
        textSize = 20.dp
    ) {

    }
}

@Preview
@Composable
private fun CommentSelectPreview() {
    CommentSelectView()
}