package com.zinc.berrybucket.ui.presentation.write.options

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.WriteKeyWord
import com.zinc.berrybucket.ui.compose.theme.*
import com.zinc.berrybucket.ui.presentation.common.IconButton
import com.zinc.berrybucket.ui.presentation.common.RoundChip
import com.zinc.berrybucket.ui.presentation.write.WriteAppBar
import com.zinc.berrybucket.ui.presentation.write.WriteAppBarClickEvent

// TODO : 스크롤 액션 수정 필요
@Composable
fun WriteSelectKeyWordScreen(
    closeClicked: () -> Unit,
    originKeyWord: List<WriteKeyWord>,
    selectedKeyWords: List<WriteKeyWord>,
    addKeyWordClicked: (List<WriteKeyWord>) -> Unit
) {

    val updateKeyWords = remember { mutableStateOf(selectedKeyWords) }
    val scrollState = rememberLazyGridState()
    val isShowDivider = remember { mutableStateOf(scrollState.firstVisibleItemIndex > 0) }

    Column {
        WriteAppBar(modifier = Modifier
            .fillMaxWidth(),
            nextButtonClickable = true,
            rightText = R.string.addDesc,
            clickEvent = {
                when (it) {
                    WriteAppBarClickEvent.CloseClicked -> {
                        closeClicked()
                    }
                    WriteAppBarClickEvent.NextClicked -> {
                        addKeyWordClicked(updateKeyWords.value)
                    }
                }
            })

        if (isShowDivider.value) {
            Divider(modifier = Modifier.fillMaxWidth(), color = Gray3)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp)
                .padding(top = 24.dp)
                .scrollable(state = scrollState, orientation = Orientation.Vertical),
            state = rememberLazyListState()
        ) {
            item {
                Text(
                    text = stringResource(id = R.string.writeSelectKeyWordGuide),
                    color = Gray10,
                    fontSize = 15.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            item {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 34.dp),
                    mainAxisSpacing = 24.dp,
                    crossAxisSpacing = 17.dp,
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
                                        } else {
                                            updateKeyWords.value += keywordItem
                                        }
                                        selected = !selected
                                    }
                                ),
                            textModifier = Modifier.padding(horizontal = 8.dp, vertical = 14.dp),
                            selectedColor = Main3,
                            unSelectedColor = Gray7,
                            text = keywordItem.text,
                            isSelected = selected
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun WriteSelectKeyWordAppBarLayout(
    modifier: Modifier,
    closeClicked: () -> Unit,
    addClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        IconButton(image = R.drawable.btn_40_close,
            contentDescription = stringResource(id = R.string.closeDesc),
            modifier = Modifier
                .padding(start = 14.dp, top = 6.dp, bottom = 6.dp)
                .size(40.dp),
            onClick = {
                closeClicked()
            })

        Text(
            text = stringResource(id = R.string.addDesc),
            color = Main4,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(10.dp)
                .clickable {
                    addClicked()
                }
                .padding(end = 18.dp)
                .align(Alignment.CenterEnd))
    }
}
