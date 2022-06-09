package com.zinc.berrybucket.ui.presentation.report

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.ReportClickEvent
import com.zinc.berrybucket.model.ReportInfo
import com.zinc.berrybucket.model.ReportItem
import com.zinc.berrybucket.model.ReportItems
import com.zinc.berrybucket.ui.compose.theme.*


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReportLayer(
    reportInfo: ReportInfo,
    reportItems: ReportItems,
    clickEvent: (ReportClickEvent) -> Unit
) {
    var currentReportItem: ReportItem by remember {
        mutableStateOf(reportItems.items.first())
    }

    BaseTheme {
        Column {
            ReportInfoLayer(reportInfo)

            Divider(
                modifier = Modifier
                    .padding(top = 28.dp)
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(color = Gray3)
            )

            ReportItemsLayer(
                reportItems = reportItems,
                selectedItem = currentReportItem,
                selectItem = { selectItem ->
                    currentReportItem = selectItem
                    clickEvent.invoke(ReportClickEvent.ReportItemSelected(selectItem))
                })

        }
    }
}

@Composable
private fun ReportInfoLayer(reportInfo: ReportInfo) {
    Column(modifier = Modifier.padding(top = 28.dp, start = 28.dp, end = 28.dp)) {
        Text(
            text = stringResource(id = R.string.reportWriter),
            fontSize = 13.sp,
            color = Gray7
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = reportInfo.writer,
            fontSize = 14.sp,
            color = Gray10
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(id = R.string.reportContents),
            fontSize = 13.sp,
            color = Gray7
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = reportInfo.contents,
            fontSize = 14.sp,
            color = Gray10,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ReportItemsLayer(
    reportItems: ReportItems,
    selectedItem: ReportItem,
    selectItem: (ReportItem) -> Unit
) {
    reportItems.items.forEach { item ->
        Column() {
            ReportItemLayer(
                item = item,
                isSelected = (item == selectedItem),
                selectItem = selectItem
            )
        }
    }
}

@Composable
private fun ReportItemLayer(
    item: ReportItem,
    isSelected: Boolean,
    selectItem: (ReportItem) -> Unit
) {
    Box(modifier = Modifier
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
                painter = if (isSelected) painterResource(id = R.drawable.ic_btn_radio_select)
                else painterResource(id = R.drawable.ic_btn_radio_unselect),
                contentDescription = stringResource(id = R.string.reportItemButton)
            )
            Text(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                text = item.text,
                fontSize = 15.sp,
                color = if (isSelected) Main4 else Gray10
            )
        }
    }
}

//@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
//@Composable
//private fun ReportTextField(onImeAction: (String) -> Unit,  textFieldFocused: (Boolean) -> Unit) {
//    val hintText = stringResource(id = R.string.reportTextFieldHint)
//    val keyboardController = LocalSoftwareKeyboardController.current
//    var reportText by remember { mutableStateOf(TextFieldValue("")) }
//    val maxChar = 500
//
//    BasicTextField(
//        value = reportText,
//        textStyle = TextStyle(
//            color = Gray10,
//            fontSize = 15.sp
//        ),
//        onValueChange = {
//            if (it.text.length <= maxChar) reportText = it
//        },
//        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
//        keyboardActions = KeyboardActions(onDone = {
//            onImeAction(reportText.text)
//            keyboardController?.hide()
//            textFieldFocused(false)
//        }),
//        decorationBox = { innerTextField ->
//            Row {
//                if (reportText.text.isEmpty()) {
//                    Text(text = hintText, color = Gray7, fontSize = 15.sp)
//                }
//                innerTextField()  //<-- Add this
//            }
//        },
//        modifier = Modifier
//            .padding(start = 28.dp, end = 28.dp, top = 24.dp)
//            .onFocusChanged { focusState ->
//                Log.e(
//                    "ayhan",
//                    "focusState : ${focusState.isFocused}, ${focusState.hasFocus}, $focusState"
//                )
//                when {
//                    focusState.isFocused -> {
//                        textFieldFocused(focusState.isFocused)
//                    }
//                }
//            })
//}
//
//@Composable
//private fun ReportButton(modifier: Modifier = Modifier, clickEvent: () -> Unit) {
//    Button(
//        onClick = { clickEvent() },
//        shape = RoundedCornerShape(12.dp),
//        colors = ButtonDefaults.buttonColors(backgroundColor = Gray1),
//        border = BorderStroke(1.dp, color = Gray10),
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(start = 28.dp, end = 28.dp, bottom = 28.dp)
//    ) {
//        Text(
//            text = "신고하기",
//            color = Gray10,
//            fontSize = 16.sp,
//            textAlign = TextAlign.Center,
//            modifier = Modifier
//                .padding(vertical = 13.dp)
//                .fillMaxWidth()
//        )
//    }
//}