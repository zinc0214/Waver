package com.zinc.waver.ui_write.presentation.options

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.waver.model.WriteKeyWord
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray2
import com.zinc.waver.ui.design.theme.Gray7
import com.zinc.waver.ui.design.theme.Main3
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.presentation.component.RoundChip
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_write.R
import com.zinc.waver.ui_write.presentation.WriteAppBar
import com.zinc.waver.ui_write.presentation.WriteAppBarClickEvent
import com.zinc.waver.ui_common.R as CommonR

// TODO : 스크롤 액션 수정 필요
@Composable
fun WriteSelectKeyWordScreen(
    closeClicked: () -> Unit,
    originKeyWord: List<WriteKeyWord>,
    selectedKeyWords: List<WriteKeyWord>,
    addKeyWordClicked: (List<WriteKeyWord>) -> Unit
) {

    val updateKeyWords = remember { mutableStateOf(selectedKeyWords) }
    val scrollState = rememberLazyListState()
    val isShowDivider = scrollState.firstVisibleItemIndex == 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Gray2)
            .statusBarsPadding()
    ) {
        WriteAppBar(
            modifier = Modifier
                .fillMaxWidth(),
            nextButtonClickable = true,
            rightText = CommonR.string.addDesc,
            clickEvent = {
                when (it) {
                    WriteAppBarClickEvent.CloseClicked -> {
                        closeClicked()
                    }

                    WriteAppBarClickEvent.NextClicked -> {
                        addKeyWordClicked(updateKeyWords.value)
                    }
                }
            },
            isShowDivider = isShowDivider
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp)
                .padding(top = 24.dp),
            state = scrollState,
            contentPadding = PaddingValues(bottom = 50.dp)
        ) {
            item {
                MyText(
                    text = stringResource(id = R.string.writeSelectKeyWordGuide),
                    color = Gray10,
                    fontSize = dpToSp(15.dp),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            item {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 34.dp),
                    maxItemsInEachRow = 3,
                    horizontalArrangement = Arrangement.spacedBy(
                        17.dp,
                        alignment = Alignment.CenterHorizontally
                    ),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    originKeyWord.forEach { keywordItem ->
                        var selected by remember { mutableStateOf(updateKeyWords.value.any { it == keywordItem }) }
                        RoundChip(
                            chipRadius = 24.dp,
                            modifier = Modifier
                                .defaultMinSize(minWidth = 90.dp, minHeight = 48.dp)
                                .selectable(
                                    selected = selected,
                                    onClick = {
                                        if (selected) {
                                            updateKeyWords.value -= keywordItem
                                            selected = !selected
                                        } else if (updateKeyWords.value.size < 3) {
                                            updateKeyWords.value += keywordItem
                                            selected = !selected
                                        }
                                    }
                                ),
                            textModifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 14.dp),
                            selectedTextColor = Main3,
                            unSelectedTextColor = Gray7,
                            text = keywordItem.name,
                            isSelected = selected,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun WriteSelectKeyWordScreenPreview() {
    val originKeyWord = listOf(
        WriteKeyWord(
            code = "1", name = "제주도"
        ),
        WriteKeyWord(
            code = "2", name = "여행"
        ),
        WriteKeyWord(
            code = "3", name = "아주아주긴키워드라서넘어가면"
        ),
        WriteKeyWord(
            code = "4", name = "안년여영ㅇ"
        )
    )
    WriteSelectKeyWordScreen(
        closeClicked = {},
        originKeyWord = originKeyWord,
        selectedKeyWords = originKeyWord,
        addKeyWordClicked = {

        },

        )
}