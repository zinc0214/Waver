package com.zinc.waver.ui_detail.screen

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.waver.model.CommentMentionInfo
import com.zinc.waver.ui.design.theme.Main1
import com.zinc.waver.ui_detail.component.mention.MentionListView
import com.zinc.waver.ui_detail.component.mention.MentionTitleView
import com.zinc.waver.ui_detail.component.mention.MentionedListView

// TODO : 사용하지 않는 태그 기능. 추후 사용 가능성이 있어서 남겨둠.
@Composable
fun CommentMentionScreen(
    modifier: Modifier = Modifier,
    validMentionList: List<CommentMentionInfo>,
    backPress: () -> Unit,
    selectedFinish: (List<CommentMentionInfo>) -> Unit
) {

    val updateMentionList = remember {
        validMentionList.toMutableStateList()
    }

    Scaffold { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .statusBarsPadding()
        ) {

            MentionTitleView(
                closeClicked = { backPress() },
                finishClicked = { selectedFinish(updateMentionList) },
                isFinishAvailable = updateMentionList.any { it.isSelected }
            )

            MentionedListView(
                mentionedList = updateMentionList.filter { it.isSelected },
                removeMention = { item ->
                    val index = updateMentionList.indexOfFirst { it == item }
                    updateMentionList[index] = updateMentionList[index].copy(isSelected = false)
                })

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .scrollable(rememberScrollState(), orientation = Orientation.Vertical)
            ) {


                item {
                    MentionListView(
                        isFriendsList = true,
                        mentionList = updateMentionList.filter { it.isSelected.not() && it.isFriend },
                        mentionSelected = { item ->
                            val index = updateMentionList.indexOfFirst { it == item }
                            updateMentionList[index] =
                                updateMentionList[index].copy(isSelected = true)
                        })
                }

                item {

                    Divider(
                        modifier = Modifier
                            .padding(vertical = 28.dp, horizontal = 24.dp)
                            .height(1.dp),
                        color = Main1
                    )
                }

                item {
                    MentionListView(
                        isFriendsList = false,
                        mentionList = updateMentionList.filter { it.isSelected.not() && it.isFriend.not() },
                        mentionSelected = { item ->
                            val index = updateMentionList.indexOfFirst { it == item }
                            updateMentionList[index] =
                                updateMentionList[index].copy(isSelected = true)
                        })
                }
            }
        }
    }

}

@Composable
@Preview
private fun CommentMentionScreenPreview() {
    val tagableNickName = mutableListOf<CommentMentionInfo>()

    repeat(10) {
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

    repeat(5) {
        tagableNickName.add(
            CommentMentionInfo(
                userId = "${it}a",
                profileImage = "",
                nickName = "에이비 $it",
                isFriend = true,
                isSelected = false
            )
        )
    }

    CommentMentionScreen(validMentionList = tagableNickName,
        modifier = Modifier,
        backPress = {},
        selectedFinish = {})
}