package com.zinc.berrybucket.compose.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.*
import com.zinc.berrybucket.model.TabType
import com.zinc.berrybucket.model.TabType.Companion.getNameResource

@Composable
fun SearchLayer(
    currentTabType: TabType
) {

    val tabType = remember { mutableStateOf(currentTabType) }

    BaseTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Gray1)
        ) {
            Column {
                TopAppBar()

                SearchEditView(
                    type = tabType.value,
                )

                Box(
                    modifier = Modifier
                        .padding(top = 15.dp, bottom = 21.dp, start = 24.dp, end = 24.dp)
                )
                {
                    Divider(
                        modifier = Modifier
                            .height(1.dp)
                            .background(color = Gray4)
                            .fillMaxWidth()
                    )
                }

                ChipBodyContent(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    list = TabType.values().toList(),
                    currentTab = tabType
                )
            }

        }

    }
}

@Composable
private fun TopAppBar() {
    TopAppBar(
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.btn40close),
                contentDescription = null,
                modifier = Modifier.padding(start = 14.dp, top = 8.dp)
            )
        },
        title = {
            Text(text = "")
        },
        backgroundColor = Gray1,
        elevation = 0.dp
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchEditView(
    type: TabType,
    text: String = stringResource(id = R.string.hint),
    onImeAction: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    Row(
        modifier = Modifier.padding(start = 28.dp, end = 28.dp)
    ) {
        Text(
            text = stringResource(id = getNameResource(type)),
            color = Main4,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.width(12.dp))

        BasicTextField(
            value = searchText,
            textStyle = TextStyle(
                color = Gray10,
                fontSize = 20.sp
            ),
            onValueChange = { searchText = it },
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                onImeAction()
                keyboardController?.hide()
            }),
            decorationBox = { innerTextField ->
                Row {
                    if (searchText.text.isEmpty()) {
                        Text(text = text, color = Gray6, fontSize = 20.sp)
                    }
                    innerTextField()  //<-- Add this
                }
            },
        )
    }

}


@Composable
fun ChipBodyContent(
    modifier: Modifier = Modifier,
    list: List<TabType>,
    currentTab: MutableState<TabType>
) {
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
    ) {
        for (tabType in list) {
            Chip(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .selectable(
                        selected = (currentTab.value == tabType),
                        onClick = {
                            currentTab.value = tabType
                        }
                    ),
                text = stringResource(id = getNameResource(tabType)),
                isSelected = currentTab.value == tabType
            )
        }

    }
}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String, isSelected: Boolean) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = if (isSelected) Main4 else Gray4, width = 1.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = 0.dp
    ) {
        Text(
            text = text,
            color = if (isSelected) Main4 else Gray6,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
        )
    }
}