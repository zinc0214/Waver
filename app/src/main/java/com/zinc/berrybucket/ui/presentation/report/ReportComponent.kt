package com.zinc.berrybucket.ui.presentation.report

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.ReportClickEvent
import com.zinc.berrybucket.ui.compose.theme.*
import com.zinc.berrybucket.ui.presentation.common.IconButton
import com.zinc.common.models.ReportInfo
import com.zinc.common.models.ReportItem
import com.zinc.common.models.ReportItems


@Composable
fun ReportTopAppBar(
    modifier: Modifier,
    isDividerVisible: Boolean,
    backButtonClicked: () -> Unit
) {

    ConstraintLayout(
        modifier = modifier
            .background(color = Gray1)
            .height(52.dp)
    ) {
        val (backButton, title, bottomDivider) = createRefs()

        IconButton(
            onClick = { backButtonClicked() },
            image = R.drawable.btn_40_back,
            modifier = Modifier
                .size(40.dp)
                .padding(start = 14.dp)
                .fillMaxHeight()
                .constrainAs(backButton) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            contentDescription = stringResource(id = R.string.back)
        )

        Text(
            text = stringResource(id = R.string.commentReportTitle),
            fontSize = 16.sp,
            color = Gray10,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .constrainAs(title) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        )


        if (isDividerVisible) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(bottomDivider) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                color = Gray10
            )
        }
    }

}

@Preview
@Composable
fun Test() {
    ReportTopAppBar(
        modifier = Modifier.fillMaxWidth(),
        isDividerVisible = true,
        backButtonClicked = {

        }
    )
}

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
            ReportInfoLayer(reportInfo)
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
                selectItem = { selectItem ->
                    currentReportItem = selectItem
                    clickEvent.invoke(ReportClickEvent.ReportItemSelected(selectItem))
                })
        }
        item {
            if (currentReportItem.text == "기타") {
                ETCTextField(
                    onImeAction = onImeAction,
                    keyboardController = keyboardController
                )
            }
        }
    }
}

@Composable
fun ReportInfoLayer(reportInfo: ReportInfo) {
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

@Composable
fun ReportItemsLayer(
    reportItems: ReportItems,
    selectedItem: ReportItem,
    selectItem: (ReportItem) -> Unit
) {
    reportItems.items.forEach { item ->
        Column {
            ReportItemLayer(
                item = item,
                isSelected = (item == selectedItem),
                selectItem = selectItem
            )
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ETCTextField(
    onImeAction: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?,
) {
    val hintText = stringResource(id = R.string.reportTextFieldHint)
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
            .padding(top = 24.dp)
    ) {

        BasicTextField(
            value = searchText,
            textStyle = TextStyle(
                color = Gray10, fontSize = 15.sp, fontWeight = FontWeight.Medium
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
                        Text(text = hintText, color = Gray7, fontSize = 15.sp)
                    }
                    innerTextField()  //<-- Add this
                }
            },
            modifier = Modifier
                .fillMaxWidth()
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
        Text(
            text = stringResource(id = R.string.reportActionButton),
            fontSize = 16.sp,
            color = Gray10,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}