package com.zinc.waver.ui_report

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zinc.common.models.ReportItem
import com.zinc.common.models.ReportItems
import com.zinc.waver.model.ReportClickEvent
import com.zinc.waver.model.ReportInfo
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray3
import com.zinc.waver.ui.design.theme.Gray7
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.presentation.component.MyTextField
import com.zinc.waver.ui.util.dpToSp
import kotlinx.coroutines.launch
import com.zinc.waver.ui_common.R as CommonR

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReportContentView(
    modifier: Modifier,
    scrollState: LazyListState,
    reportInfo: ReportInfo,
    reportItems: ReportItems,
    clickEvent: (ReportClickEvent) -> Unit,
    onImeAction: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?
) {
    var currentReportItem: ReportItem by remember { mutableStateOf(reportItems.items.first()) }

    LazyColumn(
        modifier = modifier,
        state = scrollState
    ) {
        item {
            MyText(
                text = stringResource(id = R.string.reportWriter),
                fontSize = dpToSp(13.dp),
                color = Gray7,
                modifier = Modifier.padding(top = 28.dp, start = 28.dp, end = 28.dp)
            )
        }

        item {
            MyText(
                modifier = Modifier.padding(top = 4.dp, start = 28.dp, end = 28.dp),
                text = reportInfo.writer,
                fontSize = dpToSp(14.dp),
                color = Gray10
            )
        }

        item {
            MyText(
                modifier = Modifier.padding(top = 8.dp, start = 28.dp, end = 28.dp),
                text = stringResource(id = R.string.reportContents),
                fontSize = dpToSp(13.dp),
                color = Gray7
            )
        }

        item {
            MyText(
                modifier = Modifier.padding(top = 4.dp, start = 28.dp, end = 28.dp),
                text = reportInfo.contents,
                fontSize = dpToSp(14.dp),
                color = Gray10,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        
        item {
            Divider(
                modifier = Modifier
                    .padding(top = 28.dp)
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(color = Gray3)
            )

        }
        item {
            ReportItemsLayer(
                reportItems = reportItems,
                selectedItem = currentReportItem,
                onImeAction = onImeAction,
                keyboardController = keyboardController,
                selectItem = { selectItem ->
                    currentReportItem = selectItem
                    clickEvent.invoke(ReportClickEvent.ReportItemSelected(selectItem))
                })
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReportItemsLayer(
    reportItems: ReportItems,
    selectedItem: ReportItem,
    selectItem: (ReportItem) -> Unit,
    onImeAction: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?
) {
    reportItems.items.forEach { item ->
        Column {
            ReportItemLayer(
                item = item,
                isSelected = (item == selectedItem),
                selectItem = selectItem
            )

            if (selectedItem.text == "기타" && item.text == "기타") {
                ETCTextField(
                    onImeAction = onImeAction,
                    keyboardController = keyboardController
                )
            }
        }
    }
}

@Composable
fun ReportItemLayer(
    item: ReportItem,
    isSelected: Boolean,
    selectItem: (ReportItem) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 28.dp,
                end = 28.dp,
                top = 25.dp
            )
            .clickable {
                selectItem.invoke(item)
            }) {
        Row {
            Image(
                modifier = Modifier.size(20.dp, 20.dp),
                painter = if (isSelected) painterResource(id = CommonR.drawable.ic_btn_radio_select)
                else painterResource(id = CommonR.drawable.ic_btn_radio_unselect),
                contentDescription = stringResource(id = R.string.reportItemButton)
            )
            MyText(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                text = item.text,
                fontSize = dpToSp(15.dp),
                color = if (isSelected) Main4 else Gray10
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun ETCTextField(
    onImeAction: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?,
) {
    val hintText = stringResource(id = R.string.reportTextFieldHint)
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp, vertical = 24.dp)
            .bringIntoViewRequester(bringIntoViewRequester)
    ) {

        MyTextField(
            value = searchText,
            textStyle = TextStyle(
                color = Gray10, fontSize = dpToSp(15.dp), fontWeight = FontWeight.Medium
            ),
            onValueChange = { searchText = it },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                onImeAction(searchText.text)
                keyboardController?.hide()
            }),
            decorationBox = { innerTextField ->
                Row {
                    if (searchText.text.isEmpty()) {
                        MyText(text = hintText, color = Gray7, fontSize = dpToSp(15.dp))
                    }
                    innerTextField()  //<-- Add this
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                }
        )

        Divider(
            color = Gray3,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 7.dp)
        )
    }


}

@Composable
fun ReportButton(modifier: Modifier, reportButtonClicked: () -> Unit) {
    Box(
        modifier = modifier
            .padding(horizontal = 28.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Gray1)
            .clickable {
                reportButtonClicked()
            }
            .border(
                width = 1.dp,
                color = Gray10,
                shape = RoundedCornerShape(12.dp)
            )
            .requiredHeightIn(min = 56.dp),
        contentAlignment = Alignment.Center
    ) {
        MyText(
            text = stringResource(id = R.string.reportActionButton),
            fontSize = dpToSp(16.dp),
            color = Gray10,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}